package unsch.fcsa.sigte.application.usecase.maestro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.maestro.EscuelaCommand;
import unsch.fcsa.sigte.application.port.out.maestro.projections.EscuelaProfesionalProjection;
import unsch.fcsa.sigte.domain.exception.DuplicateKeyException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarEscuelaUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.DepartamentoRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.EscuelaProfesionalRepositoryPort;

import java.util.List;

@Slf4j
public class GestionarEscuelaUseCaseImpl implements GestionarEscuelaUseCase {

    private final EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort;
    private final DepartamentoRepositoryPort departamentoRepositoryPort;

    public GestionarEscuelaUseCaseImpl(
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort,
            DepartamentoRepositoryPort departamentoRepositoryPort
    ) {
        this.escuelaProfesionalRepositoryPort = escuelaProfesionalRepositoryPort;
        this.departamentoRepositoryPort = departamentoRepositoryPort;
    }

    @Override
    @Transactional
    public EscuelaProfesional crearEscuela(EscuelaCommand command) {
        log.info("Iniciando el registro de una escuela: {}", command.denominacion());

        if (escuelaProfesionalRepositoryPort.existePorCodigo(command.codigo())) {
            throw new DuplicateKeyException("Escuela", "codigo", command.codigo());
        }

        if (escuelaProfesionalRepositoryPort.existePorAbreviatura(command.abreviatura())) {
            throw new DuplicateKeyException("Escuela", "abreviatura", command.abreviatura());
        }

        if (!departamentoRepositoryPort.existePorId(command.departamentoId())) {
            throw new ResourceNotFoundException("Departamento",  "id", command.departamentoId());
        }

        EscuelaProfesional escuelaProfesional = new EscuelaProfesional(
                command.departamentoId(),
                command.codigo(),
                command.abreviatura(),
                command.denominacion(),
                command.plantillaMemo(),
                command.plantillaRes()
        );

        return escuelaProfesionalRepositoryPort.guardar(escuelaProfesional);
    }

    @Override
    @Transactional
    public EscuelaProfesional modificarEscuela(EscuelaCommand command) {
        log.info("Actualizando un recurso. [ ESCUELA : {} ]", command.id());

        EscuelaProfesional existente = obtenerPorId(command.id());

        if (!existente.getAbreviatura().equalsIgnoreCase(command.abreviatura()) &&
                escuelaProfesionalRepositoryPort.existePorAbreviatura(command.abreviatura())) {
            throw new DuplicateKeyException("Escuela", "abreviatura", command.abreviatura());
        }

        if (!existente.getCodigo().equals(command.codigo()) &&
                escuelaProfesionalRepositoryPort.existePorCodigo(command.codigo())) {
            throw new DuplicateKeyException("Escuela", "codigo", command.codigo());
        }

        if (!existente.getDepartamentoAcademicoId().equals(command.departamentoId()) &&
                !departamentoRepositoryPort.existePorId(command.departamentoId())) {
            throw new ResourceNotFoundException("Departamento",  "id", command.departamentoId());
        }

        existente.actualizarDatos(
                command.departamentoId(),
                command.codigo(),
                command.abreviatura(),
                command.denominacion(),
                command.plantillaMemo(),
                command.plantillaRes()
        );

        return escuelaProfesionalRepositoryPort.guardar(existente);
    }

    @Override
    public EscuelaProfesional obtenerPorId(Long id) {
        log.info("Obteniendo una escuela por ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalido [ ESCUELA ]");
        }

        return escuelaProfesionalRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escuela", "id", id));
    }

    @Override
    public EscuelaProfesionalProjection obtenerDetallePorId(Long id) {
        log.info("Obteniendo una escuela a detaller por ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalido [ ESCUELA ]");
        }

        return escuelaProfesionalRepositoryPort.obtenerDetallerPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Escuela", "id", id));
    }

    @Override
    public List<EscuelaProfesional> listarTodosPorFacultad(Long facultadId) {
        return escuelaProfesionalRepositoryPort.listarTodos();
    }

    @Override
    public List<EscuelaProfesional> listarActivosPorFacultad(Long facultadId) {
        return List.of();
    }

    @Override
    public List<EscuelaProfesional> listarTodosPorDepartamento(Long departamentoId) {
        return List.of();
    }

    @Override
    public EscuelaProfesional activarEscuela(Long id) {
        return null;
    }

    @Override
    public EscuelaProfesional desactivarEscuela(Long id) {
        return null;
    }

    @Override
    public void eliminarEscuela(Long id) {
        EscuelaProfesional escuela = obtenerPorId(id);
        escuela.marcarComoEliminado();
        escuelaProfesionalRepositoryPort.guardar(escuela);
    }
}
