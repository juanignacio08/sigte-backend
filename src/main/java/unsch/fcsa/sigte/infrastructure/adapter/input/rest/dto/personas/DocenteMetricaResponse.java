package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DocenteMetricaResponse(
        Integer total,
        Integer totalActivos,
        Double porcentajeActivos,
        Integer totalInactivos,
        Double porcentajeInactivos
) {
}
