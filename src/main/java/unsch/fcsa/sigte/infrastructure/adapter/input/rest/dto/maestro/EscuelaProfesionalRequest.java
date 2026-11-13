package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EscuelaProfesionalRequest(
        @Min(value = 1, message = "El id no puede ser menor a 1.")
        Long id,

        @NotNull(message = "El ID del departamento académico es obligatorio")
        Long departamentoId,

        @NotNull(message = "El codigo es requerido")
        @Min(value = 1, message = "El codigo no puede ser menor a 1")
        Integer codigo,

        @NotBlank(message = "La abreviatura es obligatoria")
        @Size(max = 10, message = "La abreviatura no debe superar los 10 caracteres")
        String abreviatura,

        @NotBlank(message = "La denominación es obligatoria")
        @Size(max = 200, message = "La denominación no debe superar los 200 caracteres")
        String denominacion
) {
}
