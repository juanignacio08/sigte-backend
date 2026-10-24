package unsch.fcsa.sigte.application.usecase.tramites;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.tramites.ComisionCommand;
import unsch.fcsa.sigte.application.command.tramites.MiembroComisionCommand;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarComisionUseCase;
import unsch.fcsa.sigte.application.port.out.personas.DocenteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.ComisionMiembroRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.ComisionRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.TesisRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionDetalleView;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.Comision;
import unsch.fcsa.sigte.domain.model.tramites.ComisionMiembro;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
public class GestionarComisionUseCaseImpl implements GestionarComisionUseCase {

    private final ComisionRepositoryPort comisionRepositoryPort;
    private final ComisionMiembroRepositoryPort comisionMiembroRepositoryPort;
    private final TesisRepositoryPort tesisRepositoryPort;
    private final DocenteRepositoryPort docenteRepositoryPort;

    public GestionarComisionUseCaseImpl(
            ComisionRepositoryPort comisionRepositoryPort,
            ComisionMiembroRepositoryPort comisionMiembroRepositoryPort,
            TesisRepositoryPort tesisRepositoryPort,
            DocenteRepositoryPort docenteRepositoryPort
    ) {
        this.comisionRepositoryPort = comisionRepositoryPort;
        this.comisionMiembroRepositoryPort = comisionMiembroRepositoryPort;
        this.tesisRepositoryPort = tesisRepositoryPort;
        this.docenteRepositoryPort = docenteRepositoryPort;
    }

    @Override
    @Transactional
    public Comision registrar(ComisionCommand command) {
        log.info("Registrando comisión. [ TESIS : {} | ETAPA : {} ]", command.tesisId(), command.etapaCodigo());

        if (!tesisRepositoryPort.existePorId(command.tesisId())) {
            throw new ResourceNotFoundException("Tesis", "id", command.tesisId().toString());
        }

        if (comisionRepositoryPort.existeComisionActivaPorTesisYEtapa(command.tesisId(), command.etapaCodigo())) {
            throw new BusinessRuleException("Ya existe una comisión activa para esta tesis en la etapa indicada.");
        }

        Comision comision = new Comision(
                null,
                command.tesisId(),
                command.etapaCodigo(),
                command.fechaConformacion() != null ? command.fechaConformacion() : LocalDate.now(),
                1,
                false
        );

        return comisionRepositoryPort.guardar(comision);
    }

    @Override
    @Transactional
    public Comision actualizar(ComisionCommand command) {
        log.info("Actualizando comisión. [ COMISION : {} ]", command.id());
        Comision comision = comisionRepositoryPort.buscarPorId(command.id())
                .orElseThrow(() -> new ResourceNotFoundException("Comision", "id", command.id().toString()));

        if (!comision.getEtapaCodigo().equals(command.etapaCodigo())
                && comisionRepositoryPort.existeComisionActivaPorTesisYEtapa(comision.getTesisId(), command.etapaCodigo())) {
            throw new BusinessRuleException("Ya existe una comisión activa para esta tesis en la etapa indicada.");
        }

        comision.actualizarDatos(command.etapaCodigo(), command.fechaConformacion());
        return comisionRepositoryPort.guardar(comision);
    }

    @Override
    @Transactional
    public ComisionMiembro agregarMienbro(MiembroComisionCommand command) {
        log.info("Agregando miembro a comisión. [ COMISION : {} | DOCENTE : {} ]", command.comisionId(), command.docenteId());

        if (!comisionRepositoryPort.existeComisionActiva(command.comisionId())) {
            throw new ResourceNotFoundException("Comision", "id", command.comisionId().toString());
        }

        if (!docenteRepositoryPort.existePorIdActivo(command.docenteId())) {
            throw new ResourceNotFoundException("Docente", "id", command.docenteId().toString());
        }

        if (comisionMiembroRepositoryPort.existeDocenteActivoEnComision(command.comisionId(), command.docenteId())) {
            throw new BusinessRuleException("El docente ya tiene un rol activo en esta comisión.");
        }

        if (comisionMiembroRepositoryPort.existeRolSingularActivo(command.comisionId(), command.rolCodigo())) {
            throw new BusinessRuleException("Ya existe un miembro activo con el rol indicado en esta comisión.");
        }

        ComisionMiembro miembro = new ComisionMiembro(
                null, command.comisionId(), command.docenteId(), command.rolCodigo(),
                command.orden() != null ? command.orden() : 1,
                LocalDate.now(), null, null, 1, false
        );

        return comisionMiembroRepositoryPort.guardar(miembro);

    }

    @Override
    public List<ComisionDetalleView> listarPorTesis(UUID tesisId) {
        log.info("Listando las comisiones de una tesis. [ TESIS : {} ]", tesisId);
        return comisionRepositoryPort.listarPorTesis(tesisId);
    }

    @Override
    public void eliminar(Long id) {

    }

}
