package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteTesisEntity;

@Repository
public interface SpringDataExpedienteTesisRepository extends JpaRepository<ExpedienteTesisEntity, Long> {
    boolean existsByNumeroExpedienteAndEliminadoFalse(String numeroExpediente);

}
