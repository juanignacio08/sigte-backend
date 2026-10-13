package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteArchivoDetalleView;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteArchivoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteTesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExpedientePersistenceMapper {

    private final MaestroPersistenceAdapter adapter;

    public ExpedientePersistenceMapper(MaestroPersistenceAdapter adapter) {
        this.adapter = adapter;
    }

    public ExpedienteTesisEntity toEntity(ExpedienteTesis domain) {
        if (domain == null) return null;

        TesisEntity tesisEntity = TesisEntity.builder()
                .id(domain.getTesisId())
                .build();

        ExpedienteTesisEntity entity = ExpedienteTesisEntity.builder()
                .id(domain.getId())
                .tesis(tesisEntity)
                .numeroExpediente(domain.getNumeroExpediente())
                .etapaCodigo(domain.getEtapaCodigo())
                .numeroRevision(domain.getNumeroRevision())
                .fechaIngreso(domain.getFechaIngreso())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();

        if (domain.getArchivos() != null) {
            var archivosEntity = domain.getArchivos().stream().map(a ->
                    ExpedienteArchivoEntity.builder()
                            .id(a.getId())
                            .expedienteTesis(entity)
                            .nombreArchivo(a.getNombreArchivo())
                            .archivoUrl(a.getArchivoUrl())
                            .tamanioBytes(a.getTamanioBytes())
                            .estado(a.getEstado())
                            .eliminado(a.isEliminado())
                            .build()
            ).collect(Collectors.toList());
            entity.setArchivos(archivosEntity);
        }

        return entity;
    }

    public ExpedienteTesis toDomain(ExpedienteTesisEntity entity) {
        if (entity == null) return null;

        ExpedienteTesis domain = new ExpedienteTesis(
                entity.getId(),
                entity.getTesis().getId(),
                entity.getNumeroExpediente(),
                entity.getEtapaCodigo(),
                entity.getNumeroRevision(),
                entity.getFechaIngreso(),
                entity.getEstado(),
                entity.getEliminado()
        );

        if (entity.getArchivos() != null) {
            var archivosDomain = entity.getArchivos().stream().map(a ->
                    new ExpedienteArchivo(
                            a.getId(),
                            entity.getId(),
                            a.getNombreArchivo(),
                            a.getArchivoUrl(),
                            a.getTamanioBytes(),
                            a.getEstado(),
                            a.getEliminado()
                    )
            ).collect(Collectors.toList());
            domain.setArchivos(archivosDomain);
        }

        return domain;
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
                entity.getEstado(),
                null
        );
    }

    public ExpedienteDetalleView toDetalleView(ExpedienteTesisEntity entity) {
        if (entity == null) return null;

        String etapa = adapter.getItemByCode(entity.getEtapaCodigo()) != null ?
                adapter.getItemByCode(entity.getEtapaCodigo()).getDenominacion() : "NO REGISTRADO";

        List<ExpedienteArchivoDetalleView> archivosView = entity.getArchivos().stream().map(a ->
                new ExpedienteArchivoDetalleView(a.getId(), a.getNombreArchivo(), a.getArchivoUrl(), a.getTamanioBytes())
        ).collect(Collectors.toList());

        return new ExpedienteDetalleView(
                entity.getId(),
                entity.getTesis().getId(),
                entity.getNumeroExpediente(),
                entity.getEtapaCodigo(),
                etapa,
                entity.getNumeroRevision(),
                entity.getFechaIngreso(),
                entity.getEstado(),
                archivosView
        );
    }
}
