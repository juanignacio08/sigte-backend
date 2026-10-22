package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteTesisEntity;

@Component
public class ExpedientePersistenceMapper {

    private final MaestroPersistenceAdapter adapter;

    public ExpedientePersistenceMapper(MaestroPersistenceAdapter adapter) {
        this.adapter = adapter;
    }

    public ExpedienteTesisEntity toEntity(ExpedienteTesis domain) {
        if (domain == null) return null;

        return ExpedienteTesisEntity.builder()
                .id(domain.getId())
                .numeroExpediente(domain.getNumeroExpediente())
                .etapaCodigo(domain.getEtapaCodigo())
                .numeroRevision(domain.getNumeroRevision())
                .fechaIngreso(domain.getFechaIngreso())
                .motivoBaja(domain.getMotivoBaja())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();

    }

    public ExpedienteTesis toDomain(ExpedienteTesisEntity entity) {
        if (entity == null) return null;

        return new ExpedienteTesis(
                entity.getId(),
                entity.getTesis().getId(),
                entity.getNumeroExpediente(),
                entity.getEtapaCodigo(),
                entity.getNumeroRevision(),
                entity.getFechaIngreso(),
                entity.getEstado(),
                entity.getEliminado(),
                entity.getMotivoBaja()
        );
    }

    public ExpedienteDetalleView toDetalleViewWithMasterTable(ExpedienteTesisEntity entity) {
        if (entity == null) return null;

        String etapa = adapter.getItemByCode(entity.getEtapaCodigo()) != null ?
                adapter.getItemByCode(entity.getEtapaCodigo()).getDenominacion() : "NO REGISTRADO";

        return new ExpedienteDetalleView(
                entity.getId(),
                entity.getTesis().getId(),
                entity.getNumeroExpediente(),
                entity.getEtapaCodigo(),
                etapa,
                entity.getNumeroRevision(),
                entity.getFechaIngreso(),
                entity.getEstado()
        );
    }

}
