package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import lombok.Builder;

@Builder
public record   DepartamentoResponse(
        Long id,
        Long facultadId,
        String abreviatura,
        String denominacion,
        Integer estado
) {
}
