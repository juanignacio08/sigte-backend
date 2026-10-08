package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;

import java.util.UUID;

@Repository
public interface SpringDataTesisRepository extends JpaRepository<TesisEntity, UUID>, JpaSpecificationExecutor<TesisEntity> {

}
