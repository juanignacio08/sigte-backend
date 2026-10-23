package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.ExpedienteArchivoRepositoryPort;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteArchivoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataExpedienteArchivoRepository;

import java.util.List;

@Component
public class ExpedienteArchivoPersistenceAdapter implements ExpedienteArchivoRepositoryPort {

    private final SpringDataExpedienteArchivoRepository archivoRepository;

    public ExpedienteArchivoPersistenceAdapter(SpringDataExpedienteArchivoRepository archivoRepository) {
        this.archivoRepository = archivoRepository;
    }

    @Override
    public void guardarTodos(List<ExpedienteArchivo> archivos) {
        List<ExpedienteArchivoEntity> entities = archivos.stream()
                .map(this::toEntity)
                .toList();

        archivoRepository.saveAll(entities);

    }

    @Override
    public List<ExpedienteArchivo> obtenerPorExpediente(Long expedienteId) {
        return archivoRepository.findAllByExpedienteTesisId(expedienteId).stream()
                .map(this::toDomain)
                .toList();
    }

    private ExpedienteArchivoEntity toEntity(ExpedienteArchivo expedienteArchivo) {
        if (expedienteArchivo == null) return null;

        return ExpedienteArchivoEntity.builder()
                .id(expedienteArchivo.getId())
                .expedienteTesisId(expedienteArchivo.getExpedienteTesisId())
                .nombreArchivo(expedienteArchivo.getNombreArchivo())
                .archivoUrl(expedienteArchivo.getArchivoUrl())
                .tamanioBytes(expedienteArchivo.getTamanioBytes())
                .estado(expedienteArchivo.getEstado())
                .eliminado(expedienteArchivo.isEliminado())
                .build();

    }

    private ExpedienteArchivo toDomain(ExpedienteArchivoEntity entity) {
        if (entity == null) return null;

        return new ExpedienteArchivo(
                entity.getId(),
                entity.getExpedienteTesisId(),
                entity.getNombreArchivo(),
                entity.getArchivoUrl(),
                entity.getTamanioBytes(),
                entity.getEstado(),
                entity.getEliminado()
        );

    }

}
