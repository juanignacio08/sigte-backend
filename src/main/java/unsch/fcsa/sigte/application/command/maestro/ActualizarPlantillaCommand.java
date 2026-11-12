package unsch.fcsa.sigte.application.command.maestro;

import unsch.fcsa.sigte.application.command.files.ArchivoSubida;

public record ActualizarPlantillaCommand(
        Long id,
        Integer plazoDias,
        Integer plazoMeses,
        ArchivoSubida archivoDocx   // opcional: solo si se quiere reemplazar
) {
    public ActualizarPlantillaCommand {
        if (id == null)
            throw new IllegalArgumentException("El ID de la plantilla es obligatorio.");
    }
}