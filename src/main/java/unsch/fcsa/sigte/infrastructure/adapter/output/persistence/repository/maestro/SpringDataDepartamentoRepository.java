package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.application.port.out.maestro.projections.DepartamentoConTotalEscuelasView;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;

import java.util.List;

@Repository
public interface SpringDataDepartamentoRepository extends JpaRepository<DepartamentoEntity, Long> {

    boolean existsByAbreviaturaAndEliminadoFalse(String abreviatura);

    boolean existsByIdAndEliminadoFalse(Long id);

    List<DepartamentoEntity> findByFacultadIdAndEstadoAndEliminadoFalse(Long facultadId, Integer estado);
    default List<DepartamentoEntity> getActivesByFaculty(Long facultyId) {
        return findByFacultadIdAndEstadoAndEliminadoFalse(facultyId, 1);
    }

    @Query("""
            SELECT d.id AS id, 
            d.facultadId AS facultadId, 
            d.abreviatura AS abreviatura, 
            d.denominacion AS denominacion, 
            d.estado AS estado, 
            COUNT(e.id) AS totalEscuelas 
            FROM DepartamentoEntity d 
            LEFT JOIN EscuelaEntity e ON e.departamento = d AND e.eliminado = false 
            WHERE d.facultadId = :facultadId AND d.eliminado = false 
            GROUP BY d.id, d.facultadId, d.abreviatura, d.denominacion, d.estado 
            ORDER BY d.abreviatura ASC
            """)
    List<DepartamentoConTotalEscuelasView> listarConTotalEscuelasPorFacultad(@Param("facultadId") Long facultadId);
}
