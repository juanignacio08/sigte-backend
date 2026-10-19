package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public record ActualizarExpedienteRequest(
        @NotBlank(message = "El número de expediente es obligatorio")
        @Size(max = 50, message = "El número de expediente no puede superar los 50 caracteres")
        String numeroExpediente,

        @NotBlank(message = "El código de etapa es obligatorio")
        @Size(min = 6, max = 6, message = "El código de etapa debe tener exactamente 6 caracteres")
        String etapaCodigo,

        @NotNull(message = "El número de revisión es obligatorio")
        Integer numeroRevision,

        OffsetDateTime fechaIngreso
) {}
