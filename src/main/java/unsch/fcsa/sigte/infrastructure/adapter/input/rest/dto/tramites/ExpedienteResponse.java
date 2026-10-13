package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExpedienteResponse(
        Long id,
        UUID tesisId,
        String numeroExpediente,
        String etapaCodigo,
        String etapaDenominacion,
        Integer numRevision,
        OffsetDateTime fechaAsociacion,
        Integer estado
) {}
