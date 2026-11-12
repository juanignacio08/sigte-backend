package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.PlantillaDocumentoDetalleView;
import unsch.fcsa.sigte.domain.model.maestro.PlantillaDocumento;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.PlantillaDocumentoEntity;

@Component
public class PlantillaDocumentoPersistenceMapper {

    private final MaestroPersistenceAdapter maestroAdapter;

    public PlantillaDocumentoPersistenceMapper(MaestroPersistenceAdapter maestroAdapter) {
        this.maestroAdapter = maestroAdapter;
    }

    public PlantillaDocumentoEntity toEntity(PlantillaDocumento domain) {
        if (domain == null) return null;

        return PlantillaDocumentoEntity.builder()
                .id(domain.getId())
                .escuelaId(domain.getEscuelaId())
                .tipoDocumentoCodigo(domain.getTipoDocumentoCodigo())
                .nombreDocumento(domain.getNombreDocumento())
                .archivoUrl(domain.getArchivoUrl())
                .plazoDias(domain.getPlazoDias())
                .plazoMeses(domain.getPlazoMeses())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();
    }

    public PlantillaDocumento toDomain(PlantillaDocumentoEntity entity) {
        if (entity == null) return null;

        return new PlantillaDocumento(
                entity.getId(),
                entity.getEscuelaId(),
                entity.getTipoDocumentoCodigo(),
                entity.getNombreDocumento(),
                entity.getArchivoUrl(),
                entity.getPlazoDias(),
                entity.getPlazoMeses(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }

    public PlantillaDocumentoDetalleView toDetalleView(
            PlantillaDocumentoEntity entity) {
        if (entity == null) return null;

        String tipoDenom = maestroAdapter.getItemByCode(entity.getTipoDocumentoCodigo()) != null
                ? maestroAdapter.getItemByCode(entity.getTipoDocumentoCodigo()).getDenominacion()
                : "NO REGISTRADO";

        return PlantillaDocumentoDetalleView.builder()
                .id(entity.getId())
                .escuelaId(entity.getEscuelaId())
                .nombreDocumento(entity.getNombreDocumento())
                .tipoDocumentoCodigo(entity.getTipoDocumentoCodigo())
                .tipoDocumentoDenominacion(tipoDenom)
                .archivoUrl(entity.getArchivoUrl())
                .plazoDias(entity.getPlazoDias())
                .plazoMeses(entity.getPlazoMeses())
                .estado(entity.getEstado())
                .build();
    }
}