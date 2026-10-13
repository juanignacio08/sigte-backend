package unsch.fcsa.sigte.application.command.tramites;

import unsch.fcsa.sigte.application.command.files.ArchivoSubida;

import java.util.List;

public record ActualizarExpedienteCommand(
        Long id,
        String numeroExpediente,
        String etapaCodigo,
        List<Long> idsArchivosAEliminar,
        List<ArchivoSubida> nuevosArchivos
) {
    public ActualizarExpedienteCommand {
        if (id == null) {
            throw new IllegalArgumentException("El ID del expediente es obligatorio.");
        }
        if (numeroExpediente == null || numeroExpediente.isBlank()) {
            throw new IllegalArgumentException("El número de expediente es obligatorio.");
        }
        if (etapaCodigo == null || etapaCodigo.isBlank()) {
            throw new IllegalArgumentException("El código de etapa es obligatorio.");
        }
    }
}
