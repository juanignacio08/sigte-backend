package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record   DepartamentoResponse(
        Long id,
        Long facultadId,
        String abreviatura,
        String denominacion,
        Integer estado,

        Integer totalEscuelas
) {
}
