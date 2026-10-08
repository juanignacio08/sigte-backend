package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;
import unsch.fcsa.sigte.domain.model.tramites.TesisAutor;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.EstudianteEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteArchivoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteTesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisAutorEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;

import java.util.Comparator;
import java.util.List;

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

        if (entity.getExpedientes() != null) {
            List<ExpedienteTesis> expedientes = entity.getExpedientes().stream()
                    .map(e -> {
                        ExpedienteTesis exp = new ExpedienteTesis(
                                e.getId(), entity.getId(), e.getNumeroExpediente(),
                                e.getEtapaCodigo(), e.getNumeroRevision(),
                                e.getFechaIngreso(), e.getEstado(), e.getEliminado());

                        if (e.getArchivos() != null) {
                            List<ExpedienteArchivo> archivos = e.getArchivos().stream()
                                    .map(a -> new ExpedienteArchivo(
                                            a.getId(), e.getId(), a.getNombreArchivo(),
                                            a.getArchivoUrl(), a.getTamanioBytes(),
                                            a.getEstado(), a.getEliminado()
                                    )).toList();
                            exp.setArchivos(archivos);
                        }

                        return exp;
                    })
                    .toList();
            tesis.setExpedientes(expedientes);
        }

        return tesis;
    }

    public TesisEntity toEntity(Tesis domain) {
        if (domain == null) return null;

        EscuelaEntity escuela = EscuelaEntity.builder().id(domain.getEscuelaId()).build();

        TesisEntity entity = TesisEntity.builder()
                .id(domain.getId())
                .escuela(escuela)
                .titulo(domain.getTitulo())
                .tituloDictaminado(domain.getTituloDictaminado())
                .estadoCodigo(domain.getEstadoCodigo())
                .fechaRegistro(domain.getFechaRegistro())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();

        if (domain.getAutores() != null) {
            List<TesisAutorEntity> autorEntities = domain.getAutores().stream()
                    .map(a -> TesisAutorEntity.builder()
                            .id(a.getId())
                            .tesis(entity)
                            .estudiante(EstudianteEntity.builder().id(a.getEstudianteId()).build())
                            .fechaAsociacion(a.getFechaAsociacion())
                            .estado(a.getEstado())
                            .eliminado(a.isEliminado())
                            .build())
                    .toList();
            entity.setAutores(autorEntities);
        }

        if (domain.getExpedientes() != null) {
            List<ExpedienteTesisEntity> expEntities = domain.getExpedientes().stream()
                    .map(e -> {
                        ExpedienteTesisEntity expEntity = ExpedienteTesisEntity.builder()
                                .id(e.getId())
                                .tesis(entity)
                                .numeroExpediente(e.getNumeroExpediente())
                                .etapaCodigo(e.getEtapaCodigo())
                                .numeroRevision(e.getNumeroRevision())
                                .fechaIngreso(e.getFechaIngreso())
                                .estado(e.getEstado())
                                .eliminado(e.isEliminado())
                                .build();

                        if (e.getArchivos() != null) {
                            List<ExpedienteArchivoEntity> archivoEntities = e.getArchivos().stream()
                                    .map(arch -> ExpedienteArchivoEntity.builder()
                                            .id(arch.getId())
                                            .expedienteTesis(expEntity)
                                            .nombreArchivo(arch.getNombreArchivo())
                                            .archivoUrl(arch.getArchivoUrl())
                                            .tamanioBytes(arch.getTamanioBytes())
                                            .estado(arch.getEstado())
                                            .eliminado(arch.isEliminado())
                                            .build())
                                    .toList();

                            expEntity.setArchivos(archivoEntities);
                        }

                        return expEntity;
                    })
                    .toList();
            entity.setExpedientes(expEntities);
        }

        return entity;
    }

    public TesisDetalleView toTesisDetalleView(TesisEntity entity) {
        if (entity == null) return null;

        String estadoDenom = maestroAdapter.getItemByCode(entity.getEstadoCodigo()) != null ?
                maestroAdapter.getItemByCode(entity.getEstadoCodigo()).getDenominacion() : "NO REGISTRADO";

        List<TesisDetalleView.AutorView> autoresView = entity.getAutores().stream()
                .map(a -> new TesisDetalleView.AutorView(
                        a.getEstudiante().getId(),
                        a.getEstudiante().getCodigo(),
                        a.getEstudiante().getNumeroDocumento(),
                        a.getEstudiante().getNombres(),
                        a.getEstudiante().getPrimerApellido(),
                        a.getEstudiante().getSegundoApellido(),
                        a.getEstudiante().getCorreoInstitucional()
                )).toList();

        ExpedienteTesisEntity expActivo = entity.getExpedientes().stream()
                .max(Comparator.comparing(ExpedienteTesisEntity::getFechaIngreso))
                .orElse(null);

        TesisDetalleView.ExpedienteView expedienteView = null;
        if (expActivo != null) {
            String etapaDenom = maestroAdapter.getItemByCode(expActivo.getEtapaCodigo()) != null ?
                    maestroAdapter.getItemByCode(expActivo.getEtapaCodigo()).getDenominacion() : "NO REGISTRADO";

            expedienteView = new TesisDetalleView.ExpedienteView(
                    expActivo.getId(),
                    expActivo.getNumeroExpediente(),
                    expActivo.getEtapaCodigo(),
                    etapaDenom,
                    expActivo.getNumeroRevision(),
                    expActivo.getFechaIngreso()
            );
        }

        return new TesisDetalleView(
                entity.getId(),
                entity.getEscuela().getId(),
                entity.getEscuela().getDenominacion(),
                entity.getTitulo(),
                entity.getTituloDictaminado(),
                entity.getEstadoCodigo(),
                estadoDenom,
                entity.getFechaRegistro(),
                autoresView,
                expedienteView
        );
    }
}
