package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record ComisionRequest(
        @NotNull(message = "El ID de la tesis es obligatorio")
        UUID tesisId,

        @NotBlank(message = "El código de etapa es obligatorio")
        @Size(min = 6, max = 6, message = "El código de etapa debe tener exactamente 6 caracteres")
        String etapaCodigo,

        LocalDate fechaConformacion
) {}