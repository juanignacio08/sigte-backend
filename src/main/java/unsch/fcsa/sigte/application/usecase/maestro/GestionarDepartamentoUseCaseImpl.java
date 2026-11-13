package unsch.fcsa.sigte.application.usecase.maestro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.maestro.DepartamentoCommand;
import unsch.fcsa.sigte.application.port.out.maestro.EscuelaProfesionalRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.FacultadRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.DepartamentoConTotalEscuelasView;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.DepartamentoDetalle;
import unsch.fcsa.sigte.domain.exception.DuplicateKeyException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarDepartamentoUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.DepartamentoRepositoryPort;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;

import java.util.List;

@Slf4j
public class GestionarDepartamentoUseCaseImpl implements GestionarDepartamentoUseCase {

    private final DepartamentoRepositoryPort departamentoRepositoryPort;
    private final FacultadRepositoryPort facultadRepositoryPort;
    private final EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort;

    public GestionarDepartamentoUseCaseImpl(
            DepartamentoRepositoryPort departamentoRepositoryPort,
            FacultadRepositoryPort facultadRepositoryPort,
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort
    ) {
        this.departamentoRepositoryPort = departamentoRepositoryPort;
        this.facultadRepositoryPort = facultadRepositoryPort;
        this.escuelaProfesionalRepositoryPort = escuelaProfesionalRepositoryPort;
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
    @Transactional
    public DepartamentoAcademico actualizarDepartamento(DepartamentoCommand command) {
        log.info("Actualizando un recurso. [ DEPARTAMENTO : {} ]", command.id());

        DepartamentoAcademico departamentoAcademico = obtenerPorId(command.id());

        if (!departamentoAcademico.getAbreviatura().equalsIgnoreCase(command.abreviatura()) && departamentoRepositoryPort.existePorAbreviatura(command.abreviatura())) {
            log.warn("Abreviatura duplicada: {}", command.abreviatura());
            throw new DuplicateKeyException("Departamento", "abreviatura", command.abreviatura());
        }

        departamentoAcademico.actualizarDatos(command.abreviatura(), command.denominacion());

        DepartamentoAcademico saved = departamentoRepositoryPort.guardar(departamentoAcademico);
        log.info("Departamento académico actualizado exitosamente");

        return saved;
    }

    @Override
    public List<DepartamentoConTotalEscuelasView> listarTodosPorFacultad(Long facultadId) {
        log.info("Listando todos los departamentos academicos. [ FACULTAD : {} ]", facultadId);

        return departamentoRepositoryPort.listarTodosPorFacultad(facultadId);
    }

    @Override
    public DepartamentoAcademico obtenerPorId(Long id) {
        log.info("Obteniendo el departamento por ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalido [ DEPARTAMENTO ]");
        }

        return departamentoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", id));
    }

    @Override
    public DepartamentoDetalle obtenerADetallePorId(Long id) {
        log.info("Obteniendo el departamento a detalle por ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID invalido [ DEPARTAMENTO ]");
        }

        DepartamentoAcademico departamentoAcademico = departamentoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", id));

        List<EscuelaProfesional> escuelas = escuelaProfesionalRepositoryPort.listarPorDepartamento(id);

        return new DepartamentoDetalle(
                departamentoAcademico.getId(),
                departamentoAcademico.getFacultadId(),
                departamentoAcademico.getAbreviatura(),
                departamentoAcademico.getDenominacion(),
                departamentoAcademico.getEstado(),
                escuelas
        );
    }

    @Override
    public List<DepartamentoAcademico> listarActivosPorFacultad(Long facultadId) {
        log.info("Listando los departamentos academicos activos de una facultad. [ FACULTAD : {} ]", facultadId);

        return departamentoRepositoryPort.listarActivosPorFacultad(facultadId);
    }

    @Override
    @Transactional
    public DepartamentoAcademico activarDepartamento(Long id) {
        log.info("Activando departamento por ID: {}", id);

        DepartamentoAcademico departamento = obtenerPorId(id);
        if (departamento.getEstado().equals(1)) {
            return departamento;
        }

        departamento.activar();

        return departamentoRepositoryPort.guardar(departamento);
    }

    @Override
    @Transactional
    public DepartamentoAcademico desactivarDepartamento(Long id) {
        log.info("Desactivando departamento por ID: {}", id);

        DepartamentoAcademico departamento = obtenerPorId(id);
        if (departamento.getEstado().equals(0)) {
            return departamento;
        }

        departamento.desactivar();

        return departamentoRepositoryPort.guardar(departamento);
    }

    @Override
    public void eliminarDepartamento(Long id) {
        log.info("Eliminando departamento por ID: {}", id);

        DepartamentoAcademico departamento = obtenerPorId(id);
        departamento.marcarComoEliminado();

        departamentoRepositoryPort.guardar(departamento);
    }
}
