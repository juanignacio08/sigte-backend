package unsch.fcsa.sigte.application.command.tramites;

import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.domain.util.TablaMaestraCodigos;

import java.time.LocalDate;

public record RegistrarDictamenCommand(
        Long expedienteId,
        String resultadoCodigo,
        String tituloDictaminado,
        String observaciones,
        LocalDate fechaEmision,
        ArchivoSubida archivoPdf,
        ArchivoSubida archivoWord
) {
    public RegistrarDictamenCommand {
        if (expedienteId == null) {
            throw new IllegalArgumentException("El ID del expediente es obligatorio.");
        }
        if (resultadoCodigo == null || resultadoCodigo.isBlank()) {
            throw new IllegalArgumentException("El resultado del dictamen es obligatorio.");
        }
        if (!TablaMaestraCodigos.RESULTADO_DICTAMEN_APROBADO.equals(resultadoCodigo) && !TablaMaestraCodigos.RESULTADO_DICTAMEN_OBSERVADO.equals(resultadoCodigo)) {
            throw new IllegalArgumentException("El resultado debe ser APROBADO (008001) u OBSERVADO (008002).");
        }
    }
}