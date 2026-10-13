package unsch.fcsa.sigte.application.command.tramites;

import unsch.fcsa.sigte.application.command.files.ArchivoSubida;

import java.util.List;
import java.util.UUID;

public record RegistrarExpedienteCommand(
        UUID tesisId,
        String numeroExpediente,
        String etapaCodigo,
        Integer numeroRevision
) {
    public RegistrarExpedienteCommand {
        if (tesisId == null) {
            throw new IllegalArgumentException("El ID de la tesis es obligatorio.");
        }
        if (numeroExpediente == null || numeroExpediente.isBlank()) {
            throw new IllegalArgumentException("El número de expediente es obligatorio.");
        }
        if (etapaCodigo == null || etapaCodigo.isBlank()) {
            throw new IllegalArgumentException("El código de etapa es obligatorio.");
        }
    }
}
