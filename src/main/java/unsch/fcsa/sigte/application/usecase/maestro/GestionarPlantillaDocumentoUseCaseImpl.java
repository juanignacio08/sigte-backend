package unsch.fcsa.sigte.application.usecase.maestro;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.command.maestro.ActualizarPlantillaCommand;
import unsch.fcsa.sigte.application.command.maestro.RegistrarPlantillaCommand;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarPlantillaDocumentoUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.PlantillaDocumentoRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.PlantillaDocumentoDetalleView;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.maestro.PlantillaDocumento;
import unsch.fcsa.sigte.domain.util.TablaMaestraCodigos;

import java.util.List;

@Slf4j
public class GestionarPlantillaDocumentoUseCaseImpl implements GestionarPlantillaDocumentoUseCase {

    private final PlantillaDocumentoRepositoryPort repositoryPort;
    private final StoragePort storagePort;

    public GestionarPlantillaDocumentoUseCaseImpl(
            PlantillaDocumentoRepositoryPort repositoryPort,
            StoragePort storagePort) {
        this.repositoryPort = repositoryPort;
        this.storagePort = storagePort;
    }

    @Override
    @Transactional
    public PlantillaDocumento registrar(RegistrarPlantillaCommand command) {
        log.info("Registrando plantilla. [ ESCUELA : {} | TIPO : {} ]",
                command.escuelaId(), command.tipoDocumentoCodigo());

        if (repositoryPort.existePlantillaActivaPorEscuelaYTipo(command.escuelaId(), command.tipoDocumentoCodigo())) {
            throw new BusinessRuleException(
                    "Ya existe una plantilla para este tipo de documento en la escuela. " +
                            "Use la operación de actualización.");
        }

        String subdirectorio = subdirectorioPara(command.tipoDocumentoCodigo());
        String fileName = generarFileName(command.tipoDocumentoCodigo());
        String rutaRelativa = storagePort.guardarArchivo(command.archivoDocx(), subdirectorio);

        try {
            PlantillaDocumento nueva = new PlantillaDocumento(
                    null,
                    command.escuelaId(),
                    command.tipoDocumentoCodigo(),
                    fileName,
                    rutaRelativa,
                    command.plazoDias(),
                    command.plazoMeses(),
                    1,
                    false
            );

            PlantillaDocumento guardada = repositoryPort.guardar(nueva);
            log.info("Plantilla registrada. ID: {} · Ruta: {}", guardada.getId(), rutaRelativa);

            return guardada;

        } catch (Exception e) {
            try {
                storagePort.eliminarArchivo(rutaRelativa);
            } catch (Exception cleanupError) {
                log.warn("No se pudo limpiar el archivo huérfano: {}", rutaRelativa, cleanupError);
            }
            throw e;
        }
    }

    @Override
    @Transactional
    public PlantillaDocumento actualizar(ActualizarPlantillaCommand command) {
        log.info("Actualizando plantilla. [ ID : {} ]", command.id());

        PlantillaDocumento existente = repositoryPort.buscarPorId(command.id())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PlantillaDocumento", "id", command.id().toString()));

        String rutaWordAntigua = existente.getArchivoUrl();
        String nuevaRuta = rutaWordAntigua;

        if (command.archivoDocx() != null) {
            String subdirectorio = subdirectorioPara(existente.getTipoDocumentoCodigo());
            nuevaRuta = storagePort.guardarArchivo(command.archivoDocx(), subdirectorio);
            existente.actualizarArchivo(nuevaRuta);
        }

        existente.actualizarPlazos(command.plazoDias(), command.plazoMeses());
        try {
            PlantillaDocumento guardada = repositoryPort.guardar(existente);

            if (command.archivoDocx() != null && !rutaWordAntigua.equals(nuevaRuta)) {
                storagePort.eliminarTrasCommit(rutaWordAntigua);
            }

            return guardada;

        } catch (Exception e) {
            if (command.archivoDocx() != null) {
                try {
                    storagePort.eliminarArchivo(nuevaRuta);
                } catch (Exception cleanupError) {
                    log.warn("No se pudo limpiar el nuevo archivo: {}", nuevaRuta, cleanupError);
                }
            }
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PlantillaDocumentoDetalleView obtenerPorId(Long id) {
        return repositoryPort.buscarADetallePlantillaActivaPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PlantillaDocumento", "id", id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public PlantillaDocumentoDetalleView obtenerPorEscuelaYTipo(Long escuelaId, String tipoDocumentoCodigo) {
        return repositoryPort.obtenerPlantillaActivaPorEscuelaYTipo(escuelaId, tipoDocumentoCodigo)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PlantillaDocumento",
                        "escuela+tipo",
                        escuelaId + "/" + tipoDocumentoCodigo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantillaDocumentoDetalleView> listarPorEscuela(Long escuelaId) {
        return repositoryPort.listarPorEscuela(escuelaId);
    }

    @Override
    @Transactional
    public PlantillaDocumento desactivar(Long id) {
        PlantillaDocumento plantilla = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PlantillaDocumento", "id", id.toString()));

        if (plantilla.getEstado().equals(1)) {
            plantilla.desactivar();
            return repositoryPort.guardar(plantilla);
        }
        return plantilla;
    }

    @Override
    @Transactional
    public PlantillaDocumento activar(Long id) {
        PlantillaDocumento plantilla = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PlantillaDocumento", "id", id.toString()));

        if (plantilla.getEstado().equals(0)) {
            plantilla.activar();
            return repositoryPort.guardar(plantilla);
        }
        return plantilla;
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando plantilla. [ ID : {} ]", id);

        PlantillaDocumento plantilla = repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PlantillaDocumento", "id", id.toString()));

        plantilla.marcarComoEliminado();
        repositoryPort.guardar(plantilla);

        log.info("Plantilla eliminada (soft). El archivo físico se conserva.");
    }

    private String subdirectorioPara(String tipoDocumentoCodigo) {
        return switch (tipoDocumentoCodigo) {
            case TablaMaestraCodigos.TIPO_DOCUMENTO_MEMO_PROYECTO, TablaMaestraCodigos.TIPO_DOCUMENTO_MEMO_BORRADOR -> "plantillas/memorandos";
            case TablaMaestraCodigos.TIPO_DOCUMENTO_RESOLUCION_APROBACION_PROYECTO, TablaMaestraCodigos.TIPO_DOCUMENTO_RESOLUCION_FECHA_HORA -> "plantillas/resoluciones";
            default -> "plantillas/otros";
        };
    }

    private String generarFileName(String tipoDocumentoCodigo) {
        return switch (tipoDocumentoCodigo) {
            case TablaMaestraCodigos.TIPO_DOCUMENTO_MEMO_PROYECTO -> "Plantilla_Memorando_Proyecto_Tesis";
            case TablaMaestraCodigos.TIPO_DOCUMENTO_MEMO_BORRADOR -> "Plantilla_Memorando_Borrador_Tesis";
            case TablaMaestraCodigos.TIPO_DOCUMENTO_RESOLUCION_APROBACION_PROYECTO -> "Plantilla_Resolucion_Proyecto_Tesis";
            case TablaMaestraCodigos.TIPO_DOCUMENTO_RESOLUCION_FECHA_HORA -> "Plantilla_Resolucion_Fecha_Y_Hora";
            default -> "plantilla_general";
        };
    }
}