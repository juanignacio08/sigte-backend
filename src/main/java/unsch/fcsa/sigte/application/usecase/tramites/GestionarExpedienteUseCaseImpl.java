package unsch.fcsa.sigte.application.usecase.tramites;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.ActualizarExpedienteCommand;
import unsch.fcsa.sigte.application.command.tramites.RegistrarExpedienteCommand;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarExpedienteUseCase;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.application.port.out.tramites.ExpedienteArchivoRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.ExpedienteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.TesisRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteArchivoDetalleView;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.domain.util.TablaMaestraCodigos;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class GestionarExpedienteUseCaseImpl implements GestionarExpedienteUseCase {

    private final ExpedienteRepositoryPort expedienteRepositoryPort;
    private final ExpedienteArchivoRepositoryPort expedienteArchivoRepositoryPort;
    private final TesisRepositoryPort tesisRepositoryPort;
    private final StoragePort storagePort;

    public GestionarExpedienteUseCaseImpl(ExpedienteRepositoryPort expedienteRepositoryPort,
                                          ExpedienteArchivoRepositoryPort expedienteArchivoRepositoryPort,
                                          TesisRepositoryPort tesisRepositoryPort,
                                          StoragePort storagePort) {
        this.expedienteRepositoryPort = expedienteRepositoryPort;
        this.expedienteArchivoRepositoryPort = expedienteArchivoRepositoryPort;
        this.tesisRepositoryPort = tesisRepositoryPort;
        this.storagePort = storagePort;
    }

    @Override
    @Transactional
    public ExpedienteTesis registrar(RegistrarExpedienteCommand command, List<ArchivoSubida> archivos, boolean validarTesisExistente) {
        log.info("Registrando un nuevo expediente para una tesis. [ TESIS : {} ]", command.toString());
        // 1. Validar que la tesis exista
        if (validarTesisExistente && !tesisRepositoryPort.existePorId(command.tesisId())) {
            throw new ResourceNotFoundException("Tesis", "id", command.tesisId().toString());
        }

        // 2. Validar unicidad de número de expediente activo
        if (expedienteRepositoryPort.existeNumeroExpedienteActivo(command.numeroExpediente())) {
            throw new BusinessRuleException("Ya existe un expediente activo con el número: '" + command.numeroExpediente() + "'");
        }

        // 3. Validar que no exista un expediente activo con el mismo número de revision en una misma etapa
        if (validarTesisExistente && expedienteRepositoryPort.existeExpedientePorEtapaYNumeroRevisionActivo(command.tesisId(), command.etapaCodigo(), command.numeroRevision())) {
            throw new BusinessRuleException("Ya existe un expediente con la misma etapa y número de revisión");
        }

        if (archivos == null || archivos.isEmpty()) {
            throw new BusinessRuleException("Debe adjuntar al menos un archivo PDF al expediente.");
        }

        if (archivos.size() > 5) {
            throw new BusinessRuleException("No puede adjuntar más de 5 archivos PDF por expediente.");
        }

        for (ArchivoSubida archivo : archivos) {
            if (!archivo.esPdf()) {
                throw new BusinessRuleException("Solo se permiten archivos con formato PDF.");
            }
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
                    TablaMaestraCodigos.ESTADO_EXPEDIENTE_REGISTRADO,
                    null,
                    null,
                    1,
                    false,
                    null
            );

            ExpedienteTesis guardado = expedienteRepositoryPort.guardar(expediente);

            List<ExpedienteArchivo> archivosDominio = new ArrayList<>();
            for (ArchivoSubida archivo : archivos) {
                String rutaRelativa = storagePort.guardarArchivoPdf(
                        archivo,
                        "expedientes"
                );

                archivosSubidosRutas.add(rutaRelativa);

                ExpedienteArchivo archivoDom = new ExpedienteArchivo(
                        null,
                        guardado.getId(),
                        archivo.nombreOriginal(),
                        rutaRelativa,
                        archivo.tamanioBytes(),
                        1,
                        false
                );
                archivosDominio.add(archivoDom);
            }

            expedienteArchivoRepositoryPort.guardarTodos(archivosDominio);

            return guardado;

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
    public ExpedienteTesis actualizar(ActualizarExpedienteCommand command) {
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
            if (expedienteRepositoryPort.existeExpedientePorEtapaYNumeroRevisionActivo(expediente.getTesisId(), command.etapaCodigo(), command.numeroRevision())) {
                throw new BusinessRuleException("Ya existe un expediente con la misma etapa y número de revisión");
            }
        }

        // Procedemos a actualizar campos básicos (aquí requerirías métodos setters limpios o reconstrucción en dominio)
        expediente.actualizarDatos(
                command.numeroExpediente(),
                command.numeroRevision(),
                command.etapaCodigo(),
                command.fechaIngreso(),
                1,
                expediente.getEstadoCodigo()
        );

        return expedienteRepositoryPort.guardar(expediente);
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

        ExpedienteDetalleView expediente = expedienteRepositoryPort.obtenerDetallePorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", id.toString()));

        if (expediente.getArchivos() != null && !expediente.getArchivos().isEmpty()) {
            for (ExpedienteArchivoDetalleView archivo : expediente.getArchivos()) {
                try {
                    storagePort.eliminarArchivo(archivo.archivoUrl());
                } catch (Exception e) {
                    log.warn("Error al eliminar archivo. [{}]", archivo.archivoUrl(), e);
                }
            }
        }

        if (expediente.getArchivos() != null) {
            expediente.getArchivos().clear();
        }

        expedienteRepositoryPort.eliminarExpediente(expediente);
    }

    @Override
    public List<ExpedienteDetalleView> listarPorTesis(UUID tesisId) {
        log.info("Listando expedientes de una tesis. {}", tesisId.toString());
        return expedienteRepositoryPort.listarPorTesisId(tesisId);
    }

    @Override
    public List<ExpedienteDetalleView> listarPorTesisYEtapa(UUID tesisId, String etapa) {
        log.info("Listando expedientes de una tesis por etapa. [ TESIS : {} | ETAPA : {} ]", tesisId.toString(), etapa);
        return expedienteRepositoryPort.listarPorTesisIdYEtapa(tesisId, etapa);
    }

    @Override
    public ExpedienteDetalleView obtenerPorId(Long id) {
        log.info("Obteniendo a detalle un expediente. [ EXPEDIENTE : {} ]", id.toString());
        ExpedienteDetalleView detalleView = expedienteRepositoryPort.obtenerDetallePorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expediente", "id", id));

        List<ExpedienteArchivoDetalleView> archivoDetalleViews = expedienteArchivoRepositoryPort.obtenerPorExpediente(detalleView.getId())
                .stream()
                .map(this::toDetailView)
                .toList();

        detalleView.setArchivos(archivoDetalleViews);

        return detalleView;
    }

    @Override
    public ExpedienteDetalleView obtenerUltimoExpedienteTesisActivo(UUID tesisId) {
        log.info("Obteniendo ultimo detalle de expediente. [ EXPEDIENTE : {} ]", tesisId.toString());

        return expedienteRepositoryPort.obtenerDetalleUltimoExpedienteDeTesis(tesisId)
                .orElseThrow(() -> new ResourceNotFoundException("Expediente", "id", tesisId.toString()));
    }

    @Override
    public Map<UUID, ExpedienteDetalleView> obtenerUltimosExpedientes(List<UUID> tesisIds) {
        return expedienteRepositoryPort.buscarUltimosExpedientesPorTesisIds(tesisIds);
    }

    @Override
    public List<ExpedienteArchivo> listarPorExpediente(Long expedienteId) {
        return expedienteArchivoRepositoryPort.obtenerPorExpediente(expedienteId);
    }

    @Override
    public ExpedienteArchivo guardarArchivo(Long expedienteId, ArchivoSubida archivo) {
        log.info("Agregando un nuevo archivo al expediente. [ EXPEDIENTE : {} ]", expedienteId.toString());

        if (archivo == null) {
            throw new BusinessRuleException("Debe adjuntar un archivo PDF al expediente.");
        }

        if (!expedienteRepositoryPort.existePorId(expedienteId)) {
            throw new ResourceNotFoundException("Expediente", "id", expedienteId.toString());
        }

        List<ExpedienteArchivo> list = listarPorExpediente(expedienteId);

        if (list != null && !list.isEmpty() && list.size() >= 5) {
            throw new BusinessRuleException("Limite alcanzado, solo se puede registrar 5 pdfs como maximo");
        }

        if (!archivo.esPdf()) {
            throw new BusinessRuleException("Solo se permiten archivos con formato PDF.");
        }

        String rutaRelativa = null;
        try {
            rutaRelativa = storagePort.guardarArchivoPdf(
                    archivo,
                    "expedientes"
            );

            ExpedienteArchivo archivoDom = new ExpedienteArchivo(
                    null,
                    expedienteId,
                    archivo.nombreOriginal(),
                    rutaRelativa,
                    archivo.tamanioBytes(),
                    1,
                    false
            );

            return expedienteArchivoRepositoryPort.guardar(archivoDom);

        } catch (Exception e) {
            if (rutaRelativa != null) {
                storagePort.eliminarArchivo(rutaRelativa);
            }
            throw e;
        }
    }

    @Override
    public void eliminarArchivo(Long id) {
        log.info("Eliminando un archivo de expediente [ ARCHIVO-EXPEDIENTE : {} ]", id);

        ExpedienteArchivo expedienteArchivo = expedienteArchivoRepositoryPort.obtenerPorId(id);
        try {
            storagePort.eliminarArchivo(expedienteArchivo.getArchivoUrl());
        } catch (Exception e) {
            log.warn("Error al eliminar archivo. [{}]", expedienteArchivo.getArchivoUrl(), e);
        }

        expedienteArchivoRepositoryPort.eliminar(id);
    }

    @Override
    @Transactional
    public void actualizarPasoYEstadoCodigo(Long id, Integer paso, String estado) {
        if (id == null || id < 1) { throw new IllegalArgumentException("El id del expediente es invalido."); }
        if (paso == null || paso < 1) { throw new IllegalArgumentException("Paso es invalido."); }
        if (estado == null || estado.length() != 6) { throw new IllegalArgumentException("Estado invalido."); }

        ExpedienteTesis expedienteTesis = expedienteRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expediente", "id", id.toString()));

        if (!expedienteTesis.getUltimoPaso().equals(paso) || !expedienteTesis.getEstadoCodigo().equals(estado)) {
            expedienteRepositoryPort.actualizarPasoYEstado(id, paso, estado);
        }
    }

    private ExpedienteArchivoDetalleView toDetailView(ExpedienteArchivo expediente) {
        if (expediente == null) {return null;}
        return new ExpedienteArchivoDetalleView(
                expediente.getId(),
                expediente.getNombreArchivo(),
                expediente.getArchivoUrl(),
                expediente.getTamanioBytes()
        );
    }
}