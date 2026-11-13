package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteArchivoEntity;

import java.util.List;

@Repository
public interface SpringDataExpedienteArchivoRepository extends JpaRepository<ExpedienteArchivoEntity, Long> {
    List<ExpedienteArchivoEntity> findAllByExpedienteTesisId(Long expedienteId);
}
