package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.JpaRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ComisionEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataComisionRepository extends JpaRepository<ComisionEntity, Long> {

    Optional<ComisionEntity> findByIdAndEliminadoFalse(Long id);
    Optional<ComisionEntity> findByTesis_IdAndEtapaCodigoAndEliminadoFalse(UUID tesisId, String etapa);
    List<ComisionEntity> findAllByTesis_IdAndEliminadoFalse(UUID tesisId);

    boolean existsByTesis_IdAndEtapaCodigoAndEliminadoFalse(UUID tesisId, String etapaCodigo);
    boolean existsByIdAndEstadoAndEliminadoFalse(Long id, Integer estado);
}
