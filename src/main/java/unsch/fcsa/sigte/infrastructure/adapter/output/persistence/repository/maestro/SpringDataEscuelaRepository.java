package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro;

import org.springframework.data.jpa.repository.JpaRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;

import java.util.List;

public interface SpringDataEscuelaRepository extends JpaRepository<EscuelaEntity, Long> {
    boolean existsByAbreviaturaAndEliminadoFalse(String abreviatura);
    boolean existsByCodigoAndEliminadoFalse(Integer codigo);
    List<EscuelaEntity> findByEliminadoFalse();
}
