package unsch.fcsa.sigte.application.dto.maestro;

public record   DepartamentoResponse(
        Long id,
        Long facultadId,
        String abreviatura,
        String denominacion,
        Integer estado
) {
}
