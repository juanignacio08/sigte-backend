package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.spec;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import unsch.fcsa.sigte.application.command.tramites.TesisFiltro;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.EstudianteEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisAutorEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;

import java.util.ArrayList;
import java.util.List;

public class TesisSpecification {

    public static Specification<TesisEntity> conFiltro(TesisFiltro filtro) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("eliminado"), false));

            if (filtro == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            if (filtro.facultadId() != null && filtro.facultadId() > 0) {
                predicates.add(cb.equal(
                        root.get("escuela").get("departamento").get("facultadId"),
                        filtro.facultadId()
                ));
            }

            if (filtro.escuelaId() != null) {
                predicates.add(cb.equal(root.get("escuela").get("id"), filtro.escuelaId()));
            }

            if (filtro.estadoCodigo() != null && !filtro.estadoCodigo().isBlank()) {
                predicates.add(cb.equal(root.get("estadoCodigo"), filtro.estadoCodigo().trim()));
            }

            if (filtro.textoBusqueda() != null && !filtro.textoBusqueda().isBlank()) {
                String pattern = "%" + filtro.textoBusqueda().trim().toLowerCase()
                        .replaceAll("\\s+", " ") + "%";

                // Búsqueda por título de la tesis
                Predicate tituloLike = cb.like(cb.lower(root.get("titulo")), pattern);

                // Joins hacia autores -> estudiante
                Join<TesisEntity, TesisAutorEntity> autoresJoin = root.join("autores", JoinType.LEFT);
                Join<TesisAutorEntity, EstudianteEntity> estudianteJoin = autoresJoin.join("estudiante", JoinType.LEFT);

                // Número de documento del estudiante
                Predicate docLike = cb.like(
                        cb.lower(cb.coalesce(estudianteJoin.get("numeroDocumento"), "")),
                        pattern
                );

                // Nombre completo concatenado (nombres + primerApellido + segundoApellido)
                Expression<String> nombres = cb.lower(cb.coalesce(estudianteJoin.get("nombres"), ""));
                Expression<String> primerApellido = cb.lower(cb.coalesce(estudianteJoin.get("primerApellido"), ""));
                Expression<String> segundoApellido = cb.lower(cb.coalesce(estudianteJoin.get("segundoApellido"), ""));

                Expression<String> nombreCompleto = cb.concat(
                        cb.concat(
                                cb.concat(nombres, " "),
                                primerApellido
                        ),
                        cb.concat(" ", segundoApellido)
                );

                Predicate nombreCompletoLike = cb.like(nombreCompleto, pattern);

                // OR entre título, documento y nombre completo
                predicates.add(cb.or(tituloLike, docLike, nombreCompletoLike));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
