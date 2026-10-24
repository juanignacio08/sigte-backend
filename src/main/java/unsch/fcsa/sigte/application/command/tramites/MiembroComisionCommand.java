package unsch.fcsa.sigte.application.command.tramites;

public record MiembroComisionCommand(
        Long comisionId,
        Long docenteId,
        String rolCodigo,
        Integer orden
) {
    public MiembroComisionCommand {
        if (comisionId == null) {
            throw new IllegalArgumentException("El ID de la comisión es obligatorio.");
        }
        if (docenteId == null) {
            throw new IllegalArgumentException("El docente es obligatorio.");
        }
        if (rolCodigo == null || rolCodigo.isBlank()) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }
    }
}
