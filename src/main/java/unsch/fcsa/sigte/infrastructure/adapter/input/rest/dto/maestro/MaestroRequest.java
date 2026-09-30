package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MaestroRequest(
        @NotBlank(message = "El código de la tabla es requerido")
        @Size(min = 3, max = 3, message = "El código de la tabla debe de tener exactamente 3 caracteres")
        String tableCode,

        @NotBlank(message = "El código del item es requerido")
        @Size(min = 3, max = 3, message = "El código del item debe de tener exactamente 3 caracteres")
        String itemCode,

        @NotBlank(message = "La denominación es obligatoria")
        @Size(max = 200, message = "La denominación no debe superar los 200 caracteres")
        String denomination,

        Boolean isSystem
) {
}
