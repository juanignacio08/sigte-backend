package unsch.fcsa.sigte.application.command.maestro;

public record EscuelaCommand(
        Long id,
        Long departamentoId,
        Integer codigo,
        String abreviatura,
        String denominacion,
        String plantillaMemo,
        String plantillaRes
) {
    public static EscuelaCommand create(
            Long departamentoId,
            Integer codigo,
            String abreviatura,
            String denominacion,
            String plantillaMemo,
            String plantillaRes
    ) {
        return new EscuelaCommand(
                null,
                departamentoId,
                codigo,
                abreviatura,
                denominacion,
                plantillaMemo,
                plantillaRes
        );
    }

    public static EscuelaCommand update(
            Long id,
            Long departamentoId,
            Integer codigo,
            String abreviatura,
            String denominacion,
            String plantillaMemo,
            String plantillaRes
    ) {
        return new EscuelaCommand(
                id,
                departamentoId,
                codigo,
                abreviatura,
                denominacion,
                plantillaMemo,
                plantillaRes
        );
    }
}
