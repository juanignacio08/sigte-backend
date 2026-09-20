package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearDepartamentoRequest(
        @NotNull(message = "El ID de la facultad es obligatorio")
        Long facultadId,

        @NotBlank(message = "La abreviatura es obligatoria")
        @Size(max = 10, message = "La abreviatura no debe superar los 10 caracteres")
        String abreviatura,

        @NotBlank(message = "La denominación es obligatoria")
        @Size(max = 200, message = "La denominación no debe superar los 200 caracteres")
        String denominacion
) {
}
