package unsch.fcsa.sigte.application.usecase.maestro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.maestro.EscuelaCommand;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.EscuelaProfesionalProjection;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
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
    private final StoragePort storagePort;

    public GestionarEscuelaUseCaseImpl(
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort,
            DepartamentoRepositoryPort departamentoRepositoryPort,
            StoragePort storagePort
    ) {
        this.escuelaProfesionalRepositoryPort = escuelaProfesionalRepositoryPort;
        this.departamentoRepositoryPort = departamentoRepositoryPort;
        this.storagePort = storagePort;
    }

    @Override
    @Transactional
    public EscuelaProfesional crearEscuela(EscuelaCommand command, ArchivoSubida plantillaMemo, ArchivoSubida plantillaRes) {
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

        String rutaMemo = null;
        String rutaRes  = null;

        try {
            if (plantillaMemo != null) {
                rutaMemo = storagePort.guardarArchivo(plantillaMemo, "plantillas/memorandos");
            }
            if (plantillaRes != null) {
                rutaRes = storagePort.guardarArchivo(plantillaRes, "plantillas/resoluciones");
            }

            // Crear entidad de Dominio con las rutas asignadas
            EscuelaProfesional escuelaProfesional = new EscuelaProfesional(
                    command.departamentoId(),
                    command.codigo(),
                    command.abreviatura(),
                    command.denominacion(),
                    rutaMemo,
                    rutaRes
            );

            return escuelaProfesionalRepositoryPort.guardar(escuelaProfesional);

        } catch (Exception ex) {
            log.error("Error al crear la escuela. Revirtiendo archivos físicos subidos...", ex);
            if (rutaMemo != null) storagePort.eliminarArchivo(rutaMemo);
            if (rutaRes  != null) storagePort.eliminarArchivo(rutaRes);
            throw ex;
        }
    }

    @Override
    @Transactional
    public EscuelaProfesional modificarEscuela(EscuelaCommand command, ArchivoSubida plantillaMemo, ArchivoSubida plantillaRes) {
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

        String rutaMemoVieja = existente.getPlantillaMemorando();
        String rutaResVieja  = existente.getPlantillaResolucion();

        String rutaMemoFinal = rutaMemoVieja;
        String rutaResFinal  = rutaResVieja;

        String rutaMemoNueva = null;
        String rutaResNueva  = null;

        try {
            if (plantillaMemo != null) {
                rutaMemoNueva = storagePort.guardarArchivo(plantillaMemo, "plantillas/memorandos");
                rutaMemoFinal = rutaMemoNueva;
            }

            if (plantillaRes != null) {
                rutaResNueva = storagePort.guardarArchivo(plantillaRes, "plantillas/resoluciones");
                rutaResFinal = rutaResNueva;
            }

            existente.actualizarDatos(
                    command.departamentoId(),
                    command.codigo(),
                    command.abreviatura(),
                    command.denominacion(),
                    rutaMemoFinal,
                    rutaResFinal
            );

            EscuelaProfesional guardada = escuelaProfesionalRepositoryPort.guardar(existente);

            if (rutaMemoNueva != null && rutaMemoVieja != null) {
                storagePort.eliminarTrasCommit(rutaMemoVieja);
            }
            if (rutaResNueva != null && rutaResVieja != null) {
                storagePort.eliminarTrasCommit(rutaResVieja);
            }
            return guardada;

        } catch (Exception ex) {
            if (rutaMemoNueva != null) storagePort.eliminarArchivo(rutaMemoNueva);
            if (rutaResNueva  != null) storagePort.eliminarArchivo(rutaResNueva);
            throw ex;
        }
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
    public List<EscuelaProfesionalProjection> listarTodosPorFacultad(Long facultadId) {
        log.info("Lista de escuelas de una facultad. [ FACULTAD : {} ]", facultadId);

        return escuelaProfesionalRepositoryPort.listarTodosPorFacultad(facultadId);
    }

    @Override
    public List<EscuelaProfesionalProjection> listarActivosPorFacultad(Long facultadId) {
        log.info("Lista de escuelas activas de una facultad. [ FACULTAD : {} ]", facultadId);

        return escuelaProfesionalRepositoryPort.listarTodosPorFacultad(facultadId).stream()
                .filter(e -> e.getEstado().equals(1)).toList();
    }

    @Override
    public List<EscuelaProfesional> listarPorDepartamento(Long departamentoId) {
        log.info("Lista de escuelas de un departamento académico. [ DEPARTAMENTO : {} ]", departamentoId);

        return escuelaProfesionalRepositoryPort.listarPorDepartamento(departamentoId);
    }

    @Override
    @Transactional
    public EscuelaProfesional activarEscuela(Long id) {
        log.info("Activar Escuela Profesional por ID: {}", id);

        EscuelaProfesional escuelaProfesional = obtenerPorId(id);
        if (escuelaProfesional.getEstado().equals(1)) {
            return escuelaProfesional;
        }

        escuelaProfesional.activar();

        return escuelaProfesionalRepositoryPort.guardar(escuelaProfesional);
    }

    @Override
    @Transactional
    public EscuelaProfesional desactivarEscuela(Long id) {
        log.info("Desactivar Escuela Profesional por ID: {}", id);

        EscuelaProfesional escuelaProfesional = obtenerPorId(id);
        if (escuelaProfesional.getEstado().equals(0)) {
            return escuelaProfesional;
        }

        escuelaProfesional.desactivar();

        return escuelaProfesionalRepositoryPort.guardar(escuelaProfesional);
    }

    @Override
    @Transactional
    public void eliminarEscuela(Long id) {
        log.info("Eliminar Escuela Profesional por ID: {}", id);

        EscuelaProfesional escuela = obtenerPorId(id);
        escuela.marcarComoEliminado();

        String rutaMemoFinal = escuela.getPlantillaMemorando();
        String rutaResFinal = escuela.getPlantillaResolucion();

        escuelaProfesionalRepositoryPort.guardar(escuela);

        if (rutaMemoFinal != null) storagePort.eliminarArchivo(rutaMemoFinal);
        if (rutaResFinal  != null) storagePort.eliminarArchivo(rutaResFinal);

    }
}
