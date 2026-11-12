package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.PlantillaDocumentoEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataPlantillaDocumentoRepository extends JpaRepository<PlantillaDocumentoEntity, Long> {

    Optional<PlantillaDocumentoEntity> findByIdAndEliminadoFalse(Long id);

    Optional<PlantillaDocumentoEntity> findByIdAndEstadoAndEliminadoFalse(Long id, Integer estado);

    Optional<PlantillaDocumentoEntity> findByEscuelaIdAndTipoDocumentoCodigoAndEstadoAndEliminadoFalse(
            Long escuelaId, String tipoDocumentoCodigo, Integer estado);

    List<PlantillaDocumentoEntity> findByEscuelaIdAndEliminadoFalse(Long escuelaId);

    boolean existsByEscuelaIdAndTipoDocumentoCodigoAndEstadoAndEliminadoFalse(
            Long escuelaId, String tipoDocumentoCodigo, Integer estado);
}