package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

public record   DepartamentoResponse(
        Long id,
        Long facultadId,
        String abreviatura,
        String denominacion,
        Integer estado
) {
}
