package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.EstudianteEntity;

import java.util.Optional;

@Repository
public interface SpringDataEstudianteRepository extends JpaRepository<EstudianteEntity, Long>, JpaSpecificationExecutor<EstudianteEntity> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"escuela"})
    Page<EstudianteEntity> findAll(
            Specification<EstudianteEntity> spec,
            @NonNull Pageable pageable
    );

    @EntityGraph(attributePaths = {"escuela"})
    Optional<EstudianteEntity> findByIdAndEliminadoFalse(Long id);

    Optional<EstudianteEntity> findByIdEqualsAndEliminadoFalse(Long id);

    boolean existsByCodigoAndEliminadoFalse(String codigo);

    boolean existsByNumeroDocumentoAndEliminadoFalse(String numeroDocumento);

    boolean existsByCorreoInstitucional(String correoInstitucional);
}