package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteTesisEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataExpedienteTesisRepository extends JpaRepository<ExpedienteTesisEntity, Long> {
    boolean existsByNumeroExpedienteAndEliminadoFalse(String numeroExpediente);
    boolean existsByNumeroExpedienteAndEstadoAndEliminadoFalse(String numeroExpediente, Integer estado);
    boolean existsByEtapaCodigoAndNumeroRevisionAndEstadoAndEliminadoFalse(String etapaCodigo, Integer numeroRevision, Integer estado);
    List<ExpedienteTesisEntity> findByTesisIdAndEliminadoFalse(UUID tesisId);

    @EntityGraph(attributePaths = {"archivos"})
    Optional<ExpedienteTesisEntity> findByIdAndEliminadoFalse(Long id);

    Optional<ExpedienteTesisEntity> findByIdEqualsAndEliminadoFalse(Long id);
}
