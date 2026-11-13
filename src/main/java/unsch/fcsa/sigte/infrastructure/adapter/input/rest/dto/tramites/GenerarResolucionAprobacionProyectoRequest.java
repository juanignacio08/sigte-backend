package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record GenerarResolucionAprobacionProyectoRequest(
        @NotNull(message = "El ID del expediente es obligatorio")
        Long expedienteId,

        @NotNull(message = "El número de resolución es obligatorio")
        @Min(value = 1, message = "El número debe ser mayor a cero")
        Integer numeroResolucion,

        @NotNull(message = "El año es obligatorio")
        @Min(value = 2000, message = "El año debe ser válido")
        Integer anioResolucion,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fechaEmision
) {}