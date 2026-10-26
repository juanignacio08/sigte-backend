package unsch.fcsa.sigte.application.port.out.tramites.dto;

public record ExpedienteArchivoDetalleView(
        Long id,
        String nombreArchivo,
        String archivoUrl,
        Long tamanioBytes
) {}
