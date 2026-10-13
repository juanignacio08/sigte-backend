package unsch.fcsa.sigte.application.usecase.tramites;

import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.RegistrarExpedienteCommand;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarExpedienteUseCase;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.application.port.out.tramites.ExpedienteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.TesisRepositoryPort;
import unsch.fcsa.sigte.domain.exception.BusinessRuleException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
        // 1. Validar que la tesis exista
        if (!tesisRepositoryPort.existePorId(command.tesisId())) {
            throw new ResourceNotFoundException("Tesis", "id", command.tesisId().toString());
        }

        // 2. Validar unicidad de número de expediente activo
        if (expedienteRepositoryPort.existeNumeroExpedienteActivo(command.numeroExpediente())) {
            throw new BusinessRuleException("Ya existe un expediente activo con el número: '" + command.numeroExpediente() + "'");
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
                    OffsetDateTime.now(),
                    1,
                    false
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
}