package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;

@Component
public class EscuelaProfesionalMapperOut {

    public EscuelaEntity toEntity(EscuelaProfesional domain) {
        if (domain == null) return null;

        DepartamentoEntity departamento = DepartamentoEntity.builder()
                .id(domain.getDepartamentoAcademicoId())
                .build();

        return EscuelaEntity.builder()
                .id(domain.getId())
                .departamento(departamento)
                .codigo(domain.getCodigo())
                .abreviatura(domain.getAbreviatura())
                .denominacion(domain.getDenominacion())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();
    }

    public EscuelaProfesional toDomain(EscuelaEntity entity) {
        if (entity == null) return null;

        return new EscuelaProfesional(
                entity.getId(),
                entity.getDepartamento().getId(),
                entity.getCodigo(),
                entity.getAbreviatura(),
                entity.getDenominacion(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }

}
