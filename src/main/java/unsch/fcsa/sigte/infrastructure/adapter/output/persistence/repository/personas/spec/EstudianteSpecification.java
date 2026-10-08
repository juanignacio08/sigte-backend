package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import unsch.fcsa.sigte.application.command.personas.EstudianteFiltro;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.EstudianteEntity;

import java.util.ArrayList;
import java.util.List;

public class EstudianteSpecification {

    public static Specification<EstudianteEntity> conFiltro(EstudianteFiltro filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Condición implícita permanente
            predicates.add(cb.equal(root.get("eliminado"), false));

            if (filtro == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            // Filtro navegable: Facultad -> Departamento -> Escuela
            if (filtro.facultadId() != null && filtro.facultadId() > 0) {
                predicates.add(cb.equal(
                        root.get("escuela").get("departamento").get("facultadId"),
                        filtro.facultadId()
                ));
            }

            if (filtro.escuelaId() != null) {
                predicates.add(cb.equal(root.get("escuela").get("id"), filtro.escuelaId()));
            }

            if (filtro.codigo() != null && !filtro.codigo().isBlank()) {
                predicates.add(cb.like(root.get("codigo"), "%" + filtro.codigo().trim() + "%"));
            }

            if (filtro.numeroDocumento() != null && !filtro.numeroDocumento().isBlank()) {
                predicates.add(cb.like(root.get("numeroDocumento"), "%" + filtro.numeroDocumento().trim() + "%"));
            }

            if (filtro.email() != null && !filtro.email().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("correoInstitucional")),
                        "%" + filtro.email().trim().toLowerCase() + "%"
                ));
            }

            if (filtro.generoCodigo() != null && !filtro.generoCodigo().isBlank()) {
                predicates.add(cb.equal(root.get("generoCodigo"), filtro.generoCodigo().trim()));
            }

            // Búsqueda por Nombre o Apellido Paterno o Apellido Materno
            if (filtro.nombre() != null && !filtro.nombre().isBlank()) {
                String pattern = "%" + filtro.nombre().trim().toLowerCase() + "%";
                Predicate nombreLike = cb.like(cb.lower(root.get("nombres")), pattern);
                Predicate primerApellidoLike = cb.like(cb.lower(root.get("primerApellido")), pattern);
                Predicate segundoApellidoLike = cb.like(cb.lower(root.get("segundoApellido")), pattern);

                predicates.add(cb.or(nombreLike, primerApellidoLike, segundoApellidoLike));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}