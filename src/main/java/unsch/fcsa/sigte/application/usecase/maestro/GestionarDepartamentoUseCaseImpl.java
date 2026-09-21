package unsch.fcsa.sigte.application.usecase.maestro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.maestro.DepartamentoCommand;
import unsch.fcsa.sigte.application.port.out.maestro.FacultadRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.projections.DepartamentoConTotalEscuelasView;
import unsch.fcsa.sigte.domain.exception.DuplicateKeyException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarDepartamentoUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.DepartamentoRepositoryPort;

import java.util.List;

@Slf4j
public class GestionarDepartamentoUseCaseImpl implements GestionarDepartamentoUseCase {

    private final DepartamentoRepositoryPort departamentoRepositoryPort;
    private final FacultadRepositoryPort facultadRepositoryPort;

    public GestionarDepartamentoUseCaseImpl(DepartamentoRepositoryPort departamentoRepositoryPort, FacultadRepositoryPort facultadRepositoryPort) {
        this.departamentoRepositoryPort = departamentoRepositoryPort;
        this.facultadRepositoryPort = facultadRepositoryPort;
    }

    @Override
    @Transactional
    public DepartamentoAcademico crearDepartamento(DepartamentoCommand command) {
        log.info("Iniciando creación del departamento académico: {}", command.denominacion());

        if (!facultadRepositoryPort.existePorId(command.facultadId())) {
            log.warn("Intento de crear departamento en facultad inexistente ID: {}", command.facultadId());
            throw new ResourceNotFoundException("Facultad", "id", command.facultadId());
        }

        if (departamentoRepositoryPort.existePorAbreviatura(command.abreviatura())) {
            log.warn("Abreviatura duplicada detectada: {}", command.abreviatura());
            throw new DuplicateKeyException("Departamento", "abreviatura", command.abreviatura());
        }

        DepartamentoAcademico nuevoDepartamento = new DepartamentoAcademico(
                command.facultadId(), command.abreviatura(), command.denominacion()
        );

        DepartamentoAcademico saved = departamentoRepositoryPort.guardar(nuevoDepartamento);
        log.info("Departamento académico creado exitosamente con ID: {}", saved.getId());

        return saved;
    }

    @Override
    public DepartamentoAcademico obtenerPorId(Long id) {
        return departamentoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", id));
    }

    @Override
    public List<DepartamentoConTotalEscuelasView> listarTodosPorFacultad(Long facultadId) {
        log.info("Listando todos los departamentos academicos. [ FACULTAD : {} ]", facultadId);

        return departamentoRepositoryPort.listarTodosPorFacultad(facultadId);
    }

    @Override
    public DepartamentoAcademico actualizarDepartamento(Long id, String abreviatura, String denominacion) {
        DepartamentoAcademico existente = obtenerPorId(id);

        if (!existente.getAbreviatura().equalsIgnoreCase(abreviatura) &&
                departamentoRepositoryPort.existePorAbreviatura(abreviatura)) {
            throw new DuplicateKeyException("Departamento", "abreviatura", abreviatura);
        }

        existente.actualizarDatos(abreviatura, denominacion);
        return departamentoRepositoryPort.guardar(existente);
    }

    @Override
    public void eliminarDepartamento(Long id) {
        DepartamentoAcademico departamento = obtenerPorId(id);

        // Aplicamos el borrado lógico desde la entidad de dominio
        departamento.marcarComoEliminado();

        departamentoRepositoryPort.guardar(departamento);
    }
}
