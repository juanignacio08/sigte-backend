package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.FacultadEntity;

@Repository
public interface SpringDataFacultadRepository extends JpaRepository<FacultadEntity, Long> {
    boolean existsByIdAndEliminadoFalse(Long id);
}
