package unsch.fcsa.sigte.application.port.out.tramites.dto;

import lombok.Builder;

@Builder
public record ExpedienteArchivoDetalleView(
        Long id,
        String nombreArchivo,
        String archivoUrl,
        Long tamanioBytes
) {}
