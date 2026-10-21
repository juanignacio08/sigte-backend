package unsch.fcsa.sigte.application.command.tramites;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RegistrarExpedienteCommand(
        UUID tesisId,
        String numeroExpediente,
        String etapaCodigo,
        Integer numeroRevision,
        OffsetDateTime fechaIngreso
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
