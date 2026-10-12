package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas.spec;

import jakarta.persistence.criteria.Expression;
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

            // Búsqueda por Nombre Completo (nombres + primerApellido + segundoApellido)
            if (filtro.nombre() != null && !filtro.nombre().isBlank()) {
                String pattern = "%" + filtro.nombre().trim().toLowerCase().replaceAll("\\s+", " ") + "%";

                // Normalizamos nulos a cadena vacía y todo a minúsculas
                Expression<String> nombres = cb.lower(cb.coalesce(root.get("nombres"), ""));
                Expression<String> primerApellido = cb.lower(cb.coalesce(root.get("primerApellido"), ""));
                Expression<String> segundoApellido = cb.lower(cb.coalesce(root.get("segundoApellido"), ""));

                // JPA estándar: concat solo acepta 2 argumentos → anidamos
                Expression<String> nombreCompleto = cb.concat(
                        cb.concat(
                                cb.concat(nombres, " "),
                                primerApellido
                        ),
                        cb.concat(" ", segundoApellido)
                );

                predicates.add(cb.like(nombreCompleto, pattern));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}