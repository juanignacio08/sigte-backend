package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.DocenteEntity;

import java.util.Optional;

@Repository
public interface SpringDataDocenteRepository extends JpaRepository<DocenteEntity, Long> {
    boolean existsByCorreoInstitucional(String correoInstitucional);
    boolean existsByTipoDocumentoCodigoAndNumeroDocumento(String tipoDocumentoCodigo, String numeroDocumento);

    Optional<DocenteEntity> findByIdAndEliminadoFalse(Long id);
}
