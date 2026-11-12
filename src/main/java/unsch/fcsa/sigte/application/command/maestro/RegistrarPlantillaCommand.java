package unsch.fcsa.sigte.application.command.maestro;

import unsch.fcsa.sigte.application.command.files.ArchivoSubida;

public record RegistrarPlantillaCommand(
        Long escuelaId,
        String tipoDocumentoCodigo,
        Integer plazoDias,
        Integer plazoMeses,
        ArchivoSubida archivoDocx
) {
    public RegistrarPlantillaCommand {
        if (escuelaId == null)
            throw new IllegalArgumentException("La escuela es obligatoria.");
        if (tipoDocumentoCodigo == null || tipoDocumentoCodigo.isBlank())
            throw new IllegalArgumentException("El tipo de documento es obligatorio.");
        if (archivoDocx == null)
            throw new IllegalArgumentException("El archivo .docx es obligatorio.");
    }
}