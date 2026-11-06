package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.GenerarMemoCommand;
import unsch.fcsa.sigte.application.command.tramites.RegistrarDictamenCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoTesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.DocumentoTesisResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.GenerarMemoRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.RegistrarDictamenRequest;

@Component
public class DocumentoTesisRestMapper {

    public GenerarMemoCommand toCommand(GenerarMemoRequest request) {
        return new GenerarMemoCommand(
                request.tesisId(),
                request.expedienteId(),
                request.numeroCorrelativo(),
                request.anio()
        );
    }

    public RegistrarDictamenCommand toCommand(RegistrarDictamenRequest request,
                                              ArchivoSubida pdf,
                                              ArchivoSubida word) {
        return new RegistrarDictamenCommand(
                request.expedienteId(),
                request.resultadoCodigo(),
                request.observaciones(),
                request.fechaEmision(),
                pdf,
                word
        );
    }

    public DocumentoTesisResponse toResponse(DocumentoTesis domain) {
        if (domain == null) return null;

        return DocumentoTesisResponse.builder()
                .id(domain.getId())
                .expedienteTesisId(domain.getExpedienteTesisId())
                .tipoDocumentoCodigo(domain.getTipoDocumentoCodigo())
                .numeroCorrelativo(domain.getNumeroCorrelativo())
                .anioCorrelativo(domain.getAnioCorrelativo())
                .fechaEmision(domain.getFechaEmision())
                .archivoWordUrl(domain.getArchivoWordUrl())
                .archivoPdfUrl(domain.getArchivoPdfUrl())
                .tamanioBytesWord(domain.getTamanioBytesWord())
                .tamanioBytesPdf(domain.getTamanioBytesPdf())
                .resultadoCodigo(domain.getResultadoCodigo())
                .observaciones(domain.getObservaciones())
                .estadoCodigo(domain.getEstadoCodigo())
                .build();
    }

    public DocumentoTesisResponse toResponse(DocumentoTesisDetalleView view) {
        if (view == null) return null;

        return DocumentoTesisResponse.builder()
                .id(view.getId())
                .expedienteTesisId(view.getExpedienteTesisId())
                .tipoDocumentoCodigo(view.getTipoDocumentoCodigo())
                .tipoDocumentoDenominacion(view.getTipoDocumentoDenominacion())
                .numeroCorrelativo(view.getNumeroCorrelativo())
                .anioCorrelativo(view.getAnioCorrelativo())
                .numeroCompleto(componerNumeroCompleto(view))
                .fechaEmision(view.getFechaEmision())
                .archivoWordUrl(view.getArchivoWordUrl())
                .archivoPdfUrl(view.getArchivoPdfUrl())
                .tamanioBytesWord(view.getTamanioBytesWord())
                .tamanioBytesPdf(view.getTamanioBytesPdf())
                .resultadoCodigo(view.getResultadoCodigo())
                .resultadoDenominacion(view.getResultadoDenominacion())
                .observaciones(view.getObservaciones())
                .estadoCodigo(view.getEstadoCodigo())
                .estadoDenominacion(view.getEstadoDenominacion())
                .build();
    }

    private String componerNumeroCompleto(DocumentoTesisDetalleView view) {
        if (view.getNumeroCorrelativo() == null || view.getAnioCorrelativo() == null) {
            return null;
        }
        String number = String.format("%03d", view.getNumeroCorrelativo());
        return String.format("%s-%d-%s",
                number,
                view.getAnioCorrelativo(),
                "UNSCH-FCSA-D");
    }
}
