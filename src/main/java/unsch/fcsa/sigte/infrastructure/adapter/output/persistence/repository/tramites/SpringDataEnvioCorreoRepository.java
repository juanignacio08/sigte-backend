package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.EnvioCorreoEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataEnvioCorreoRepository extends JpaRepository<EnvioCorreoEntity, Long> {

    Optional<EnvioCorreoEntity> findByIdAndEliminadoFalse(Long id);

    List<EnvioCorreoEntity> findByExpedienteIdAndEliminadoFalseOrderByFechaEnvioDesc(Long expedienteTesisId);

    boolean existsByExpedienteIdAndEstadoAndEliminadoFalse(Long expedienteTesisId, Integer estado);
}