package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteTesisEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataExpedienteTesisRepository extends JpaRepository<ExpedienteTesisEntity, Long> {
    boolean existsByNumeroExpedienteAndEliminadoFalse(String numeroExpediente);
    boolean existsByNumeroExpedienteAndEstadoAndEliminadoFalse(String numeroExpediente, Integer estado);
    boolean existsByTesis_IdAndEtapaCodigoAndNumeroRevisionAndEstadoAndEliminadoFalse(UUID id, String etapaCodigo, Integer numeroRevision, Integer estado);
    List<ExpedienteTesisEntity> findByTesisIdAndEliminadoFalse(UUID tesisId);

    Optional<ExpedienteTesisEntity> findByIdAndEliminadoFalse(Long id);

    Optional<ExpedienteTesisEntity> findFirstByTesis_IdAndEliminadoFalseOrderByFechaIngresoDescIdDesc(UUID tesisId);
    default Optional<ExpedienteTesisEntity> obtenerUltimoExpedienteDeTesis(UUID tesisId) {
        return findFirstByTesis_IdAndEliminadoFalseOrderByFechaIngresoDescIdDesc(tesisId);
    }

    @Query("""
    SELECT e FROM ExpedienteTesisEntity e
    WHERE e.tesis.id IN :tesisIds
      AND e.eliminado = false
      AND e.estado = 1
    ORDER BY e.tesis.id, e.fechaIngreso DESC, e.id DESC
    """)
    List<ExpedienteTesisEntity> findExpedientesPorTesisIds(@Param("tesisIds") List<UUID> tesisIds);

}
