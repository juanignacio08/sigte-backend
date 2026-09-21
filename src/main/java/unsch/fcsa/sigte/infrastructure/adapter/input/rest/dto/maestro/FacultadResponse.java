package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FacultadResponse(
        Long id,
        String abreviatura,
        String denominacion,
        Integer estado
) {

}
