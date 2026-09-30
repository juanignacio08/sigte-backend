package unsch.fcsa.sigte.application.command.maestro;

public record EscuelaCommand(
        Long id,
        Long departamentoId,
        Integer codigo,
        String abreviatura,
        String denominacion
) {
    public static EscuelaCommand create(
            Long departamentoId,
            Integer codigo,
            String abreviatura,
            String denominacion
    ) {
        return new EscuelaCommand(
                null,
                departamentoId,
                codigo,
                abreviatura,
                denominacion
        );
    }

    public static EscuelaCommand update(
            Long id,
            Long departamentoId,
            Integer codigo,
            String abreviatura,
            String denominacion
    ) {
        return new EscuelaCommand(
                id,
                departamentoId,
                codigo,
                abreviatura,
                denominacion
        );
    }
}
