package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Comision;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ComisionEntity;

@Component
public class ComisionPersistenceMapper {

    private final MaestroPersistenceAdapter maestroAdapter;

    public ComisionPersistenceMapper(MaestroPersistenceAdapter maestroAdapter) {
        this.maestroAdapter = maestroAdapter;
    }

    public ComisionEntity toEntity(Comision domain) {
        if (domain == null) return null;
        return ComisionEntity.builder()
                .id(domain.getId())
                .etapaCodigo(domain.getEtapaCodigo())
                .fechaConformacion(domain.getFechaConformacion())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();
    }

    public Comision toDomain(ComisionEntity entity) {
        if (entity == null) return null;
        return new Comision(
                entity.getId(),
                entity.getTesis().getId(),
                entity.getEtapaCodigo(),
                entity.getFechaConformacion(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }

    public ComisionDetalleView toView(ComisionEntity entity) {
        if (entity == null) return null;

        String etapa = maestroAdapter.getItemByCode(entity.getEtapaCodigo()) != null ?
                maestroAdapter.getItemByCode(entity.getEtapaCodigo()).getDenominacion() : "NO REGISTRADO";

        return new ComisionDetalleView(
                entity.getId(),
                entity.getTesis().getId(),
                entity.getEtapaCodigo(),
                etapa,
                entity.getFechaConformacion(),
                entity.getEstado()
        );
    }
}
