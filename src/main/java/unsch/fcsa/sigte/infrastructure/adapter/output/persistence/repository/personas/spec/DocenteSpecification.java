package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteFiltro;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.DocenteEntity;

import java.util.ArrayList;
import java.util.List;

public class DocenteSpecification {
    public static Specification<DocenteEntity> conFiltro(DocenteFiltro filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(root.get("eliminado"), false));

            if (filtro.facultadId() != null && filtro.facultadId() > 0) {
                predicates.add(cb.equal(root.get("departamentoAcademico").get("facultadId"), filtro.facultadId()));
            }

            if (filtro.departamentoAcademicoId() != null && filtro.departamentoAcademicoId() > 0) {
                predicates.add(cb.equal(root.get("departamentoAcademico").get("id"), filtro.departamentoAcademicoId()));
            }

            if (filtro.escuelaId() != null && filtro.escuelaId() > 0) {
                predicates.add(cb.equal(root.get("escuela").get("id"), filtro.escuelaId()));
            }

            if (filtro.estado() != null) {
                predicates.add(cb.equal(root.get("estado"), filtro.estado()));
            }

            if (filtro.generoCodigo() != null && !filtro.generoCodigo().isBlank()) {
                predicates.add(cb.equal(root.get("generoCodigo"), filtro.generoCodigo().trim()));
            }

            if (filtro.numeroDocumento() != null && !filtro.numeroDocumento().isBlank()) {
                predicates.add(cb.like(root.get("numeroDocumento"), "%" + filtro.numeroDocumento().trim() + "%"));
            }

            if (filtro.correo() != null && !filtro.correo().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("correoInstitucional")), "%" + filtro.correo().trim().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
