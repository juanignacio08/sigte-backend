package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlantillaDocumentoResponse(
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