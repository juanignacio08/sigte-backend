package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;

import java.util.List;

@Repository
public interface SpringDataDepartamentoRepository extends JpaRepository<DepartamentoEntity, Long> {

    boolean existsByAbreviaturaAndEliminadoFalse(String abreviatura);

    boolean existsByIdAndEliminadoFalse(Long id);

    List<DepartamentoEntity> findByEliminadoFalse();
}
