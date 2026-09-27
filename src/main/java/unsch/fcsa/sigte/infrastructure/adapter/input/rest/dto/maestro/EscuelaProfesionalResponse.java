package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EscuelaProfesionalResponse(
        Long id,
        Long departamentoId,
        Integer codigo,
        String abreviatura,
        String denominacion,
        String plantillaMemo,
        String plantillaRes,
        Integer estado
) {
}
