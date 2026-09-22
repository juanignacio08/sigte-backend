package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;

import java.time.LocalDateTime;

@Component
public class DepartamentoMapperOut {

    public DepartamentoEntity toEntity(DepartamentoAcademico domain) {
        if (domain == null) return null;

        return DepartamentoEntity.builder()
                .id(domain.getId())
                .facultadId(domain.getFacultadId())
                .abreviatura(domain.getAbreviatura())
                .denominacion(domain.getDenominacion())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();
    }

    public DepartamentoAcademico toDomain(DepartamentoEntity entity) {
        if (entity == null) return null;

        return new DepartamentoAcademico(
                entity.getId(),
                entity.getFacultadId(),
                entity.getAbreviatura(),
                entity.getDenominacion(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }

}
