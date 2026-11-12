package unsch.fcsa.sigte.application.port.out.maestro.dtos;

import lombok.Builder;

@Builder
public record PlantillaDocumentoDetalleView(
        Long id,
        Long escuelaId,
        String nombreDocumento,
        String tipoDocumentoCodigo,
        String tipoDocumentoDenominacion,
        String archivoUrl,
        Integer plazoDias,
        Integer plazoMeses,
        Integer estado
) {}