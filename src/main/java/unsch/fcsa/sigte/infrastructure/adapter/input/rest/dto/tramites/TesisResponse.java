package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TesisResponse(
        UUID id,
        Long escuelaId,
        String escuelaDenominacion,
        String titulo,
        String tituloDictaminado,
        String estadoCodigo,
        String estadoDenominacion,
        OffsetDateTime fechaRegistro,
        List<TesisAutorResponse> autores,
        ExpedienteTesisResponse expedienteActivo
) {
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record TesisAutorResponse(
            Long id,
            UUID tesisId,
            Long estudianteId,
            String estudianteNombres,
            String estudiantePrimerApellido,
            String estudianteSegundoApellido,
            String estudianteDNI,
            String estudianteCorreo,
            OffsetDateTime fechaAsociacion,
            Integer estado
    ) {}

    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ExpedienteTesisResponse(
            Long id,
            UUID tesisId,
            String numeroExpediente,
            String etapaCodigo,
            String etapaDenominacion,
            Integer numRevision,
            OffsetDateTime fechaAsociacion,
            Integer estado
    ) {}
}
