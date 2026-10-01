package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MaestroResponse (
        Long id,
        String tableCode,
        String itemCode,
        String code,
        String denomination,
        boolean isSystem,
        Integer state
) {
}
