package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.dto.AutorView;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;
import unsch.fcsa.sigte.domain.model.tramites.TesisAutor;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.EstudianteEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisAutorEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TesisPersistenceMapper {

    private final MaestroPersistenceAdapter maestroAdapter;

    public TesisPersistenceMapper(MaestroPersistenceAdapter maestroAdapter) {
        this.maestroAdapter = maestroAdapter;
    }

    public Tesis toDomain(TesisEntity entity) {
        if (entity == null) return null;

        Tesis tesis = new Tesis(
                entity.getId(),
                entity.getEscuela().getId(),
                entity.getTitulo(),
                entity.getTituloDictaminado(),
                entity.getEstadoCodigo(),
                entity.getFechaRegistro(),
                entity.getEstado(),
                entity.getEliminado()
        );

        if (entity.getAutores() != null) {
            List<TesisAutor> autores = entity.getAutores().stream()
                    .map(a -> new TesisAutor(a.getId(), entity.getId(), a.getEstudiante().getId(), a.getFechaAsociacion(), a.getEstado(), a.getEliminado()))
                    .toList();
            tesis.setAutores(autores);
        }

        return tesis;
    }

    public TesisEntity toEntity(Tesis domain) {
        if (domain == null) return null;

        TesisEntity entity = TesisEntity.builder()
                .id(domain.getId())
                .titulo(domain.getTitulo())
                .tituloDictaminado(domain.getTituloDictaminado())
                .estadoCodigo(domain.getEstadoCodigo())
                .fechaRegistro(domain.getFechaRegistro())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();

        if (domain.getAutores() != null) {
            Set<TesisAutorEntity> autorEntities = domain.getAutores().stream()
                    .map(a -> TesisAutorEntity.builder()
                            .id(a.getId())
                            .tesis(entity)
                            .estudiante(EstudianteEntity.builder().id(a.getEstudianteId()).build())
                            .fechaAsociacion(a.getFechaAsociacion())
                            .estado(a.getEstado())
                            .eliminado(a.isEliminado())
                            .build())
                    .collect(Collectors.toSet());
            entity.setAutores(autorEntities);
        }

        return entity;
    }

    public TesisDetalleView toTesisDetalleView(TesisEntity entity) {
        if (entity == null) return null;

        String estadoDenom = maestroAdapter.getItemByCode(entity.getEstadoCodigo()) != null ?
                maestroAdapter.getItemByCode(entity.getEstadoCodigo()).getDenominacion() : "NO REGISTRADO";

        List<AutorView> autoresView = entity.getAutores().stream()
                .map(a -> new AutorView(
                        a.getId(),
                        a.getEstudiante().getId(),
                        a.getEstudiante().getNumeroDocumento(),
                        a.getEstudiante().getNombres(),
                        a.getEstudiante().getPrimerApellido(),
                        a.getEstudiante().getSegundoApellido(),
                        a.getEstudiante().getCorreoInstitucional(),
                        a.getFechaAsociacion(),
                        a.getEstado()
                )).toList();

        return new TesisDetalleView(
                entity.getId(),
                entity.getEscuela().getId(),
                entity.getEscuela().getDenominacion(),
                entity.getTitulo(),
                entity.getTituloDictaminado(),
                entity.getEstadoCodigo(),
                estadoDenom,
                entity.getFechaRegistro(),
                autoresView
        );
    }
}
