package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.EscuelaProfesionalProjection;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataEscuelaRepository extends JpaRepository<EscuelaEntity, Long> {
    boolean existsByAbreviaturaAndEliminadoFalse(String abreviatura);
    boolean existsByCodigoAndEliminadoFalse(Integer codigo);
    List<EscuelaEntity> findByEliminadoFalse();

    List<EscuelaEntity> findByDepartamento_IdAndEliminadoFalse(Long departamentId);

    @Query("""
            SELECT e.id AS id, 
            e.codigo AS codigo, 
            e.abreviatura AS abreviatura, 
            e.denominacion AS denominacion, 
            e.plantillaMemorando AS plantillaMemorando,
            e.plantillaResolucion AS plantillaResolucion,
            e.estado AS estado, 
            d.id AS departamentoId,
            d.denominacion AS departamentoDenominacion
            FROM EscuelaEntity e 
            LEFT JOIN DepartamentoEntity d ON d = e.departamento AND d.eliminado = false 
            WHERE e.id = :escuelaId AND e.eliminado = false 
            """)
    Optional<EscuelaProfesionalProjection> getDetailByID(@Param("escuelaId") Long escuelaId);

    @Query("""
            SELECT e.id AS id, 
            e.codigo AS codigo, 
            e.abreviatura AS abreviatura, 
            e.denominacion AS denominacion, 
            e.plantillaMemorando AS plantillaMemorando,
            e.plantillaResolucion AS plantillaResolucion,
            e.estado AS estado, 
            d.id AS departamentoId,
            d.denominacion AS departamentoDenominacion
            FROM EscuelaEntity e 
            LEFT JOIN DepartamentoEntity d ON d = e.departamento AND d.eliminado = false 
            WHERE e.eliminado = false AND d.facultadId = :facultadId  
            """)
    List<EscuelaProfesionalProjection> getDetailByFacultyId(@Param("facultadId") Long facultadId);
}
