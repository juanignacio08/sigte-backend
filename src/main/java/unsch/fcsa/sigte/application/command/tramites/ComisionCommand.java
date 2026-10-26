package unsch.fcsa.sigte.application.command.tramites;

import java.time.LocalDate;
import java.util.UUID;

public record ComisionCommand(
        Long id,
        UUID tesisId,
        String etapaCodigo,
        LocalDate fechaConformacion
) {
    public ComisionCommand {
        if (tesisId == null) {
            throw new IllegalArgumentException("El ID de la tesis es obligatorio.");
        }
        if (etapaCodigo == null || etapaCodigo.isBlank()) {
            throw new IllegalArgumentException("El código de etapa es obligatorio.");
        }
    }
}