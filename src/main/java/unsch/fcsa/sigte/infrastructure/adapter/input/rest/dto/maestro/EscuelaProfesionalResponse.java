package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

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
