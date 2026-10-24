package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ComisionMiembroEntity;

import java.util.List;
import java.util.Optional;

public interface SpringDataComisionMiembroRepository extends JpaRepository<ComisionMiembroEntity, Long> {
    Optional<ComisionMiembroEntity> findByIdAndEliminadoFalse(Long id);
    List<ComisionMiembroEntity> findByComisionIdAndEstadoAndEliminadoFalse(Long comisionId, Integer estado);

    @EntityGraph(attributePaths = {"docente"})
    List<ComisionMiembroEntity> findByComisionIdAndEliminadoFalse(Long comisionId);

    boolean existsByComisionIdAndDocente_Id(Long comisionId, Long docenteId);
    boolean existsByComisionIdAndRolCodigo(Long comisionId, String rol);

}
