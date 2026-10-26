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
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionMiembroDetalleView;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.Comision;
import unsch.fcsa.sigte.domain.model.tramites.ComisionMiembro;
import unsch.fcsa.sigte.domain.util.TablaMaestraCodigos;

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

        boolean rolMultiple = switch (command.rolCodigo()) {
            case TablaMaestraCodigos.MIEMBRO_JURADO,
                 TablaMaestraCodigos.MIEMBRO_COASESOR -> true;
            default -> false;
        };

        if (!rolMultiple && comisionMiembroRepositoryPort.existeRolSingularActivo(command.comisionId(), command.rolCodigo())) {
            throw new BusinessRuleException("Ya existe un miembro activo con el rol indicado en esta comisión.");
        }

        int orden = switch (command.rolCodigo()) {
            case TablaMaestraCodigos.MIEMBRO_PRESIDENTE -> 2;
            case TablaMaestraCodigos.MIEMBRO_JURADO -> 3;
            case TablaMaestraCodigos.MIEMBRO_ASESOR -> 4;
            case TablaMaestraCodigos.MIEMBRO_COASESOR -> 5;
            case TablaMaestraCodigos.MIEMBRO_SECRETARIO -> 6;
            default -> 1;
        };

        ComisionMiembro miembro = new ComisionMiembro(
                null, command.comisionId(), command.docenteId(), command.rolCodigo(),
                orden,
                LocalDate.now(), null, null, 1, false
        );

        return comisionMiembroRepositoryPort.guardar(miembro);

    }

    @Override
    public ComisionMiembro actualizarRol(Long id, String rol) {
        log.info("Actualizando el rol de un miembro. [ MIEMBRO : {} | ROL : {} ]", id, rol);

        if (id == null || id < 1) { throw new IllegalArgumentException("Id Incorrecto. [ COMISION ]"); }

        if (rol == null || rol.length() != 6) { throw new IllegalArgumentException("Rol Incorrecto. [ MIEMBRO ]"); }

        ComisionMiembro miembro = comisionMiembroRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comision", "id", id));

        if (miembro.getRolCodigo().equals(rol)) {
            return miembro;
        }

        boolean rolMultiple = switch (rol) {
            case TablaMaestraCodigos.MIEMBRO_JURADO,
                 TablaMaestraCodigos.MIEMBRO_COASESOR -> true;
            default -> false;
        };

        if (!rolMultiple && comisionMiembroRepositoryPort.existeRolSingularActivo(miembro.getComisionId(), rol)) {
            throw new BusinessRuleException("Ya existe un miembro activo con el rol indicado en esta comisión.");
        }

        int orden = switch (rol) {
            case TablaMaestraCodigos.MIEMBRO_PRESIDENTE -> 2;
            case TablaMaestraCodigos.MIEMBRO_JURADO -> 3;
            case TablaMaestraCodigos.MIEMBRO_ASESOR -> 4;
            case TablaMaestraCodigos.MIEMBRO_COASESOR -> 5;
            case TablaMaestraCodigos.MIEMBRO_SECRETARIO -> 6;
            default -> 1;
        };

        miembro.actualizarRol(rol, orden);

        return comisionMiembroRepositoryPort.guardar(miembro);
    }

    @Override
    public void quitarMiembro(Long id) {
        log.info("Eliminando un miembro de la comision [ COMISION-MIEMBRO : {} ]", id);
        if (comisionMiembroRepositoryPort.existePorId(id)) {
            comisionMiembroRepositoryPort.eliminar(id);
        } else {
            throw new ResourceNotFoundException("Comision-Miembro", "id", id.toString());
        }
    }

    @Override
    public ComisionDetalleView obtenerPorTesisYEtapa(UUID tesisId, String etapa) {
        log.info("Obteniendo comision de una tesis en una etapa. [ TESIS : {} | ETAPA : {} ]", tesisId, etapa);

        ComisionDetalleView comision = comisionRepositoryPort.obtenerPorTesisYEtapa(tesisId, etapa)
                .orElseThrow(() -> new ResourceNotFoundException("Comision", "id", tesisId.toString()));

        List<ComisionMiembroDetalleView> list = comisionMiembroRepositoryPort.listarDetallePorComision(comision.getId());

        comision.setMiembros(list);

        return comision;
    }

    @Override
    public ComisionDetalleView obtenerComisionDetalle(Long comisionId) {
        log.info("Obteniendo a detalle la informacion de una comision. [ COMISION : {} ]", comisionId);

        ComisionDetalleView comision = comisionRepositoryPort.buscarPorId(comisionId)
                .orElseThrow(() -> new ResourceNotFoundException("Comision", "id", comisionId.toString()));

        List<ComisionMiembroDetalleView> list = comisionMiembroRepositoryPort.listarDetallePorComision(comision.getId());

        comision.setMiembros(list);

        return comision;
    }

    @Override
    public List<ComisionDetalleView> listarPorTesis(UUID tesisId) {
        log.info("Listando las comisiones de una tesis. [ TESIS : {} ]", tesisId);
        return comisionRepositoryPort.listarPorTesis(tesisId);
    }

}
