package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisAutorEntity;

@Repository
public interface SpringDataTesisAutorRepository extends JpaRepository<TesisAutorEntity, Long> {
    boolean existsByEstudiante_IdAndEliminadoFalse(Long estudianteId);
}
