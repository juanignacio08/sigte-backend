package unsch.fcsa.sigte.application.usecase.tramites;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.ActualizarExpedienteCommand;
import unsch.fcsa.sigte.application.command.tramites.RegistrarExpedienteCommand;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarExpedienteUseCase;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.application.port.out.tramites.ExpedienteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.TesisRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class GestionarExpedienteUseCaseImpl implements GestionarExpedienteUseCase {

    private final ExpedienteRepositoryPort expedienteRepositoryPort;
    private final TesisRepositoryPort tesisRepositoryPort;
    private final StoragePort storagePort;

    public GestionarExpedienteUseCaseImpl(ExpedienteRepositoryPort expedienteRepositoryPort,
                                          TesisRepositoryPort tesisRepositoryPort,
                                          StoragePort storagePort) {
        this.expedienteRepositoryPort = expedienteRepositoryPort;
        this.tesisRepositoryPort = tesisRepositoryPort;
        this.storagePort = storagePort;
    }

    @Override
    public ExpedienteTesis registrar(RegistrarExpedienteCommand command, List<ArchivoSubida> archivos) {
        log.info("Registrando un nuevo expediente para una tesis. [ TESIS : {} ]", command.toString());
        // 1. Validar que la tesis exista
        if (!tesisRepositoryPort.existePorId(command.tesisId())) {
            throw new ResourceNotFoundException("Tesis", "id", command.tesisId().toString());
        }

        // 2. Validar unicidad de número de expediente activo
        if (expedienteRepositoryPort.existeNumeroExpedienteActivo(command.numeroExpediente())) {
            throw new BusinessRuleException("Ya existe un expediente activo con el número: '" + command.numeroExpediente() + "'");
        }

        // 3. Validar que no exista un expediente activo con el mismo número de revision en una misma etapa
        if (expedienteRepositoryPort.existeExpedientePorEtapaYNumeroRevisionActivo(command.etapaCodigo(), command.numeroRevision())) {
            throw new BusinessRuleException("Ya existe un expediente con la misma etapa y número de revisión");
        }

        if (archivos == null || archivos.isEmpty()) {
            throw new BusinessRuleException("Debe adjuntar al menos un archivo PDF al expediente.");
        }

        if (archivos.size() > 5) {
            throw new BusinessRuleException("No puede adjuntar más de 5 archivos PDF por expediente.");
        }

        List<String> archivosSubidosRutas = new ArrayList<>();
        try {
            ExpedienteTesis expediente = new ExpedienteTesis(
                    null,
                    command.tesisId(),
                    command.numeroExpediente(),
                    command.etapaCodigo(),
                    command.numeroRevision(),
                    command.fechaIngreso() != null ? command.fechaIngreso() : OffsetDateTime.now(),
                    1,
                    false,
                    null
            );

            List<ExpedienteArchivo> archivosDominio = new ArrayList<>();
            for (ArchivoSubida archivo : archivos) {
                if (!archivo.esPdf()) {
                    throw new BusinessRuleException("Solo se permiten archivos con formato PDF.");
                }
                String rutaRelativa = storagePort.guardarArchivoPdf(
                        archivo,
                        "expedientes"
                );

                archivosSubidosRutas.add(rutaRelativa);

                ExpedienteArchivo archivoDom = new ExpedienteArchivo(
                        null,
                        null,
                        archivo.nombreOriginal(),
                        rutaRelativa,
                        archivo.tamanioBytes(),
                        1,
                        false
                );
                archivosDominio.add(archivoDom);
            }

            expediente.setArchivos(archivosDominio);

            return expedienteRepositoryPort.guardar(expediente);

        } catch (Exception e) {
            // Rollback de archivos físicos en disco si falla la transacción/BD
            for (String ruta : archivosSubidosRutas) {
                if (ruta != null) {
                    storagePort.eliminarArchivo(ruta);
                }
            }
            throw e;
        }
    }

    @Override
    @Transactional
    public ExpedienteTesis actualizar(ActualizarExpedienteCommand command, List<ArchivoSubida> nuevosArchivos) {
        log.info("Actualizando un expediente. [ EXPEDIENTE : {} ]", command.id());
        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(command.id())
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", command.id().toString()));

        // TODO: Verificar si existen revisiones formales asociadas (revisionRepositoryPort.existsByExpedienteTesisId(id))
        // Si ya fue notificado/revisado, bloquear la edición.

        // Validar si cambió el número de expediente y ya existe otro activo
        if (!expediente.getNumeroExpediente().equals(command.numeroExpediente())) {
            if (expedienteRepositoryPort.existeNumeroExpedienteActivo(command.numeroExpediente())) {
                throw new BusinessRuleException("Ya existe un expediente activo con el número: '" + command.numeroExpediente() + "'");
            }
        }

        if (!expediente.getEtapaCodigo().equals(command.etapaCodigo())
                || !expediente.getNumeroRevision().equals(command.numeroRevision())) {
            if (expedienteRepositoryPort.existeExpedientePorEtapaYNumeroRevisionActivo(command.etapaCodigo(), command.numeroRevision())) {
                throw new BusinessRuleException("Ya existe un expediente con la misma etapa y número de revisión");
            }
        }

        // Procedemos a actualizar campos básicos (aquí requerirías métodos setters limpios o reconstrucción en dominio)
        expediente.actualizarDatos(
                command.numeroRevision(),
                command.etapaCodigo(),
                command.fechaIngreso()
        );

        // Para efectos prácticos, manejamos la colección de archivos
        List<String> nuevosArchivosRutas = new ArrayList<>();
        try {
            // Eliminar archivos seleccionados
            if (command.idsArchivosAEliminar() != null && !command.idsArchivosAEliminar().isEmpty()) {
                expediente.getArchivos().removeIf(archivo -> {
                    if (command.idsArchivosAEliminar().contains(archivo.getId())) {
                        storagePort.eliminarTrasCommit(archivo.getArchivoUrl());
                        return true;
                    }
                    return false;
                });
            }

            // Agregar nuevos archivos si los hay
            if (nuevosArchivos != null && !nuevosArchivos.isEmpty()) {
                int totalArchivosFuturo = expediente.getArchivos().size() + nuevosArchivos.size();
                if (totalArchivosFuturo < 1 || totalArchivosFuturo > 5) {
                    throw new BusinessRuleException("El expediente debe tener entre 1 y 5 archivos PDF en total.");
                }

                for (ArchivoSubida archivo : nuevosArchivos) {
                    if (!archivo.esPdf()) {
                        throw new BusinessRuleException("Solo se permiten archivos PDF.");
                    }
                    String rutaRelativa = storagePort.guardarArchivoPdf(archivo, "expedientes");
                    nuevosArchivosRutas.add(rutaRelativa);

                    ExpedienteArchivo nuevoArchivo = new ExpedienteArchivo(
                            null, expediente.getId(), archivo.nombreOriginal(), rutaRelativa, archivo.tamanioBytes(), 1, false
                    );
                    expediente.getArchivos().add(nuevoArchivo);
                }
            }

            return expedienteRepositoryPort.guardar(expediente);

        } catch (Exception e) {
            for (String ruta : nuevosArchivosRutas) {
                if (ruta != null) {
                    storagePort.eliminarArchivo(ruta);
                }
            }
            throw e;
        }
    }

    @Override
    public void activar(Long id) {
        log.info("Activando un expediente. [ EXPEDIENTE : {} ]", id);
        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", id.toString()));

        if (!expediente.getEstado().equals(1)) {
            expediente.activar();

            expedienteRepositoryPort.guardar(expediente);
        }
    }

    @Override
    public void desactivar(Long id, String motivo) {
        log.info("Desactivando expediente [ EXPEDIENTE : {} ]", id);
        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", id.toString()));

        if (!expediente.getEstado().equals(0)) {
            expediente.desactivar(motivo);

            expedienteRepositoryPort.guardar(expediente);
        }
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando un expediente. [ EXPEDIENTE : {} ]", id);

        // TODO: Validar que el expediente no tenga revisiones formales asociadas antes de permitir borrarlo.
        // if (revisionRepositoryPort.existsByExpedienteTesisId(id)) {
        //     throw new BusinessRuleException("No se puede eliminar un expediente que ya cuenta con revisiones.");
        // }

        ExpedienteTesis expediente = expedienteRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", id.toString()));

        if (expediente.getArchivos() != null && !expediente.getArchivos().isEmpty()) {
            for (ExpedienteArchivo archivo : expediente.getArchivos()) {
                try {
                    storagePort.eliminarArchivo(archivo.getArchivoUrl());
                } catch (Exception e) {
                    log.warn("Error al eliminar archivo. [{}]", archivo.getArchivoUrl());
                }
            }
        }

        expediente.getArchivos().clear();

        expedienteRepositoryPort.eliminarExpediente(expediente);
    }

    @Override
    public List<ExpedienteDetalleView> listarPorTesis(UUID tesisId) {
        log.info("Listando expedientes de una tesis. {}", tesisId.toString());
        return expedienteRepositoryPort.listarPorTesisId(tesisId);
    }

    @Override
    public ExpedienteDetalleView obtenerPorId(Long id) {
        log.info("Obteniendo a detalle un expediente. [ EXPEDIENTE : {} ]", id.toString());
        return expedienteRepositoryPort.obtenerDetallePorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expediente", "id", id));
    }
}