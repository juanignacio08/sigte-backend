package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.ExpedienteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteTesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites.ExpedientePersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataExpedienteTesisRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ExpedientePersistenceAdapter implements ExpedienteRepositoryPort {

    private final SpringDataExpedienteTesisRepository expedienteRepository;
    private final ExpedientePersistenceMapper mapper;

    private final EntityManager em;

    public ExpedientePersistenceAdapter(SpringDataExpedienteTesisRepository expedienteRepository,
                                        ExpedientePersistenceMapper mapper,
                                        EntityManager em) {
        this.expedienteRepository = expedienteRepository;
        this.mapper = mapper;
        this.em = em;
    }

    @Override
    public ExpedienteTesis guardar(ExpedienteTesis expediente) {
        TesisEntity tesisEntity = em.getReference(TesisEntity.class, expediente.getTesisId());
        ExpedienteTesisEntity entity = mapper.toEntity(expediente);
        entity.setTesis(tesisEntity);
        ExpedienteTesisEntity saved = expedienteRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ExpedienteTesis> buscarPorId(Long id) {
        return expedienteRepository.findByIdAndEliminadoFalse(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ExpedienteDetalleView> obtenerDetallePorId(Long id) {
        return expedienteRepository.findByIdAndEliminadoFalse(id)
                .map(mapper::toDetalleViewWithMasterTable);
    }

    @Override
    public List<ExpedienteDetalleView> listarPorTesisId(UUID tesisId) {
        return expedienteRepository.findByTesisIdAndEliminadoFalse(tesisId).stream()
                .map(mapper::toDetalleViewWithMasterTable)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeNumeroExpedienteActivo(String numeroExpediente) {
        return expedienteRepository.existsByNumeroExpedienteAndEstadoAndEliminadoFalse(numeroExpediente, 1);
    }

    @Override
    public boolean existeExpedientePorEtapaYNumeroRevisionActivo(UUID id, String etapaCodigo, Integer numeroRevision) {
        return expedienteRepository.existsByTesis_IdAndEtapaCodigoAndNumeroRevisionAndEstadoAndEliminadoFalse(id, etapaCodigo, numeroRevision, 1);
    }

    @Override
    public void eliminarExpediente(ExpedienteDetalleView expediente) {
        ExpedienteTesisEntity entity = expedienteRepository.findById(expediente.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ExpedienteTesis", "id", expediente.getId().toString()));

        expedienteRepository.delete(entity);
    }
}
