package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.Facultad;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.FacultadEntity;

@Component
public class FacultadMapperOut {

    public Facultad toDomain(FacultadEntity entity) {
        if ( entity == null) return null;

        return new Facultad(
                entity.getId(),
                entity.getAbreviatura(),
                entity.getDenominacion(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }

}
