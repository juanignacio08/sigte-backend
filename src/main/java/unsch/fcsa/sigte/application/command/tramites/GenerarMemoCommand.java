package unsch.fcsa.sigte.application.command.tramites;

import java.util.UUID;

public record GenerarMemoCommand(
        UUID tesisId,
        Long expedienteId,
        Integer numeroCorrelativo,
        Integer anio
) {
    public GenerarMemoCommand {
        if (tesisId == null) throw new IllegalArgumentException("El ID de la tesis es obligatorio.");
        if (expedienteId == null) throw new IllegalArgumentException("El ID del expediente es obligatorio.");
        if (numeroCorrelativo == null || numeroCorrelativo <= 0) throw new IllegalArgumentException("El número correlativo es obligatorio y mayor a cero.");
        if (anio == null || anio < 2015) throw new IllegalArgumentException("El año es obligatorio y debe ser válido.");
    }
}