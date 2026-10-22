package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites;

import jakarta.annotation.Nonnull;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataTesisRepository extends JpaRepository<TesisEntity, UUID>, JpaSpecificationExecutor<TesisEntity> {

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"escuela", "autores", "autores.estudiante"})
    Page<TesisEntity> findAll(
            Specification<TesisEntity> spec,
            @NonNull Pageable pageable
    );

    @EntityGraph(attributePaths = {"escuela", "autores", "autores.estudiante"})
    Optional<TesisEntity> findByIdAndEliminadoFalse(UUID id);

    boolean existsById(@NonNull UUID id);
}
