package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ComisionResponse (
    Long id,
    UUID tesisId,
    String etapaCodigo,
    String etapaDenominacion,
    LocalDate fechaConformacion,
    Integer estado,
    List<ComisionMiembroResponse> miembros
)
{}