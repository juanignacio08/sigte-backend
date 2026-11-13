package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComisionMiembroRequest(
        @NotNull(message = "La comision es obligatorio")
        Long comisionId,

        @NotNull(message = "El docente es obligatorio")
        Long docenteId,

        @NotBlank(message = "El rol es obligatorio")
        @Size(min = 6, max = 6, message = "El código de rol debe tener exactamente 6 caracteres")
        String rolCodigo,

        Integer orden
) {
}
