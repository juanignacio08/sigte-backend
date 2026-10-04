package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteMetricasProjection;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.DocenteEntity;

import java.util.Optional;

@Repository
public interface SpringDataDocenteRepository extends JpaRepository<DocenteEntity, Long>, JpaSpecificationExecutor<DocenteEntity> {
    boolean existsByCorreoInstitucional(String correoInstitucional);
    boolean existsByTipoDocumentoCodigoAndNumeroDocumento(String tipoDocumentoCodigo, String numeroDocumento);
    boolean existsByCodigoAndEliminadoFalse(String codigo);
    Optional<DocenteEntity> findByTipoDocumentoCodigoAndNumeroDocumento(String tipoDocumentoCodigo, String numeroDocumento);
    Optional<DocenteEntity> findByIdAndEliminadoFalse(Long id);

    @Query(""" 
    SELECT
        COUNT(d) AS total,
        COALESCE(SUM(CASE WHEN d.estado = 1 THEN 1L ELSE 0L END), 0L) AS totalActivos,
        COALESCE(SUM(CASE WHEN d.estado = 0 THEN 1L ELSE 0L END), 0L) AS totalInactivos
    FROM DocenteEntity d
    JOIN d.departamentoAcademico de
    WHERE d.eliminado = false
      AND de.eliminado = false
      AND de.facultadId = :facultadId
    """)
    DocenteMetricasProjection obtenerResumenConteo(@Param("facultadId") Long facultadId);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"escuela", "departamentoAcademico"})
    Page<DocenteEntity> findAll(
            Specification<DocenteEntity> spec,
            @NonNull Pageable pageable
    );
}
