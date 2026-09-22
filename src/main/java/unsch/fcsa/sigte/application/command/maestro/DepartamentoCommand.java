package unsch.fcsa.sigte.application.command.maestro;

public record DepartamentoCommand(
        Long id,
        Long facultadId,
        String abreviatura,
        String denominacion
) {
    public static DepartamentoCommand create(Long facultadId, String abreviatura, String denominacion) {
        return new DepartamentoCommand(null, facultadId, abreviatura, denominacion);
    }

    public static DepartamentoCommand update(Long id, Long facultadId, String abreviatura, String denominacion) {
        return new DepartamentoCommand(id, facultadId, abreviatura, denominacion);
    }
}
