package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DocumentoTesisResponse(
        Long id,
        Long expedienteTesisId,
        String tipoDocumentoCodigo,
        String tipoDocumentoDenominacion,
        Integer numeroCorrelativo,
        Integer anioCorrelativo,
        String numeroCompleto,
        LocalDate fechaEmision,
        String archivoWordUrl,
        String archivoPdfUrl,
        Long tamanioBytesWord,
        Long tamanioBytesPdf,
        String resultadoCodigo,
        String resultadoDenominacion,
        String observaciones,
        String estadoCodigo,
        String estadoDenominacion
) {}
