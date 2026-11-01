package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GenerarMemoRequest(
        @NotNull(message = "El ID de la tesis es obligatorio")
        UUID tesisId,

        @NotNull(message = "El ID del expediente es obligatorio")
        Long expedienteId,

        @NotNull(message = "El número correlativo es obligatorio")
        @Min(value = 1, message = "El número correlativo debe ser mayor a cero")
        Integer numeroCorrelativo,

        @NotNull(message = "El año es obligatorio")
        @Min(value = 2000, message = "El año debe ser válido")
        Integer anio
) {}