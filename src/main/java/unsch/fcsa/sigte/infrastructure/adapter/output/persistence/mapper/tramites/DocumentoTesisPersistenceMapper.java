package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoTesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.DocumentoTesisEntity;

@Component
public class DocumentoTesisPersistenceMapper {

    private final MaestroPersistenceAdapter maestroAdapter;

    public DocumentoTesisPersistenceMapper(MaestroPersistenceAdapter maestroAdapter) {
        this.maestroAdapter = maestroAdapter;
    }

    public DocumentoTesisEntity toEntity(DocumentoTesis domain) {
        if (domain == null) return null;

        return DocumentoTesisEntity.builder()
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
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();
    }

    public DocumentoTesis toDomain(DocumentoTesisEntity entity) {
        if (entity == null) return null;

        return new DocumentoTesis(
                entity.getId(),
                entity.getTesis().getId(),
                entity.getExpedienteTesisId(),
                entity.getTipoDocumentoCodigo(),
                entity.getNumeroCorrelativo(),
                entity.getAnioCorrelativo(),
                entity.getFechaEmision(),
                entity.getArchivoWordUrl(),
                entity.getArchivoPdfUrl(),
                entity.getTamanioBytesWord(),
                entity.getTamanioBytesPdf(),
                entity.getResultadoCodigo(),
                entity.getObservaciones(),
                entity.getEstadoCodigo(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }

    public DocumentoTesisDetalleView toDetalleView(DocumentoTesisEntity entity) {
        if (entity == null) return null;

        String tipoDenominacion = maestroAdapter.getItemByCode(entity.getTipoDocumentoCodigo()) != null
                ? maestroAdapter.getItemByCode(entity.getTipoDocumentoCodigo()).getDenominacion()
                : "NO REGISTRADO";

        String resultadoDenominacion = entity.getResultadoCodigo() != null
                && maestroAdapter.getItemByCode(entity.getResultadoCodigo()) != null
                ? maestroAdapter.getItemByCode(entity.getResultadoCodigo()).getDenominacion()
                : null;

        String estadoDenominacion = maestroAdapter.getItemByCode(entity.getEstadoCodigo()) != null
                ? maestroAdapter.getItemByCode(entity.getEstadoCodigo()).getDenominacion()
                : "NO REGISTRADO";

        return new DocumentoTesisDetalleView(
                entity.getId(),
                entity.getExpedienteTesisId(),
                entity.getTipoDocumentoCodigo(),
                tipoDenominacion,
                entity.getNumeroCorrelativo(),
                entity.getAnioCorrelativo(),
                entity.getFechaEmision(),
                entity.getArchivoWordUrl(),
                entity.getArchivoPdfUrl(),
                entity.getTamanioBytesWord(),
                entity.getTamanioBytesPdf(),
                entity.getResultadoCodigo(),
                resultadoDenominacion,
                entity.getObservaciones(),
                entity.getEstadoCodigo(),
                estadoDenominacion
        );
    }
}
