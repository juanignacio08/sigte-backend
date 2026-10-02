package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.DocenteEntity;

import java.util.Optional;

@Repository
public interface SpringDataDocenteRepository extends JpaRepository<DocenteEntity, Long>, JpaSpecificationExecutor<DocenteEntity> {
    boolean existsByCorreoInstitucional(String correoInstitucional);
    boolean existsByTipoDocumentoCodigoAndNumeroDocumento(String tipoDocumentoCodigo, String numeroDocumento);

    Optional<DocenteEntity> findByIdAndEliminadoFalse(Long id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"escuela", "departamentoAcademico"})
    Page<DocenteEntity> findAll(
            Specification<DocenteEntity> spec,
            @NonNull Pageable pageable
    );
}
