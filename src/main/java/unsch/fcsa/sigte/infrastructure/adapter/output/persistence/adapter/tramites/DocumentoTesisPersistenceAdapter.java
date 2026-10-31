package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.DocumentoTesisRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoTesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.DocumentoTesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites.DocumentoTesisPersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataDocumentoTesisRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class DocumentoTesisPersistenceAdapter implements DocumentoTesisRepositoryPort {

    private final SpringDataDocumentoTesisRepository repository;
    private final DocumentoTesisPersistenceMapper mapper;
    private final EntityManager em;

    public DocumentoTesisPersistenceAdapter(
            SpringDataDocumentoTesisRepository repository,
            DocumentoTesisPersistenceMapper mapper,
            EntityManager em
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.em = em;
    }

    @Override
    public DocumentoTesis guardar(DocumentoTesis documento) {
        TesisEntity tesisRef = em.getReference(TesisEntity.class, documento.getTesisId());
        DocumentoTesisEntity entity = mapper.toEntity(documento);
        entity.setTesis(tesisRef);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<DocumentoTesis> buscarPorId(Long id) {
        return repository.findByIdAndEliminadoFalse(id).map(mapper::toDomain);
    }

    @Override
    public List<DocumentoTesisDetalleView> listarPorExpediente(Long expedienteId) {
        return repository
                .findByExpedienteTesisIdAndEliminadoFalseOrderByFechaEmisionDesc(expedienteId)
                .stream()
                .map(mapper::toDetalleView)
                .toList();
    }

    @Override
    public List<DocumentoTesisDetalleView> listarPorTesis(UUID tesisId) {
        return repository
                .findByTesis_IdAndEliminadoFalseOrderByFechaEmisionDesc(tesisId)
                .stream()
                .map(mapper::toDetalleView)
                .toList();
    }

    @Override
    public List<DocumentoTesisDetalleView> listarPorExpedienteYTipo(Long expedienteId, String tipoDocumentoCodigo) {
        return repository
                .findByExpedienteTesisIdAndTipoDocumentoCodigoAndEliminadoFalse(expedienteId, tipoDocumentoCodigo)
                .stream()
                .map(mapper::toDetalleView)
                .toList();
    }

    @Override
    public boolean existeCorrelativo(String tipoDocumentoCodigo, Integer anio, Integer numero) {
        return repository.existsByTipoDocumentoCodigoAndAnioCorrelativoAndNumeroCorrelativoAndEliminadoFalse(
                tipoDocumentoCodigo, anio, numero);
    }
}
