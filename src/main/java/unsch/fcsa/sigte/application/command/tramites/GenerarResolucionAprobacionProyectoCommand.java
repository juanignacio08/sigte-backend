package unsch.fcsa.sigte.application.command.tramites;

import java.time.LocalDate;

public record GenerarResolucionAprobacionProyectoCommand (
    Long expedienteId,
    Integer numeroResolucion,
    Integer anioResolucion,
    LocalDate fechaEmision
) {
    public GenerarResolucionAprobacionProyectoCommand {
        if (expedienteId == null)
            throw new IllegalArgumentException("El ID del expediente es obligatorio.");
        if (numeroResolucion == null || numeroResolucion <= 0)
            throw new IllegalArgumentException("El número de resolución es obligatorio.");
        if (anioResolucion == null || anioResolucion < 2000)
            throw new IllegalArgumentException("El año de resolución es obligatorio.");
    }
}
