package unsch.fcsa.sigte.application.command.tramites;

import java.time.OffsetDateTime;
import java.util.List;

public record ActualizarExpedienteCommand(
        Long id,
        String numeroExpediente,
        String etapaCodigo,
        Integer numeroRevision,
        OffsetDateTime fechaIngreso
) {
    public ActualizarExpedienteCommand {
        if (id == null) {
            throw new IllegalArgumentException("El ID del expediente es obligatorio.");
        }
        if (numeroExpediente == null || numeroExpediente.isBlank()) {
            throw new IllegalArgumentException("El número de expediente es obligatorio.");
        }
        if (etapaCodigo == null || etapaCodigo.isBlank()) {
            throw new IllegalArgumentException("El código de etapa es obligatorio.");
        }
        if (numeroRevision == null || numeroRevision < 1) {
            throw new IllegalArgumentException("El número de revision es obligatorio.");
        }
    }
}
