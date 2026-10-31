package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.DocumentoTesisEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataDocumentoTesisRepository extends JpaRepository<DocumentoTesisEntity, Long> {

    Optional<DocumentoTesisEntity> findByIdAndEliminadoFalse(Long id);
    List<DocumentoTesisEntity> findByExpedienteTesisIdAndEliminadoFalseOrderByFechaEmisionDesc(Long expedienteTesisId);
    List<DocumentoTesisEntity> findByTesis_IdAndEliminadoFalseOrderByFechaEmisionDesc(java.util.UUID tesisId);
    List<DocumentoTesisEntity> findByExpedienteTesisIdAndTipoDocumentoCodigoAndEliminadoFalse(Long expedienteTesisId, String tipoDocumentoCodigo);

    boolean existsByTipoDocumentoCodigoAndAnioCorrelativoAndNumeroCorrelativoAndEliminadoFalse(
            String tipoDocumentoCodigo, Integer anioCorrelativo, Integer numeroCorrelativo);
}

