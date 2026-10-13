package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.port.out.tramites.ExpedienteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ExpedienteTesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites.ExpedientePersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataExpedienteTesisRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataTesisRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ExpedientePersistenceAdapter implements ExpedienteRepositoryPort {

    private final SpringDataExpedienteTesisRepository expedienteRepository;
    private final SpringDataTesisRepository tesisRepository;
    private final ExpedientePersistenceMapper mapper;

    public ExpedientePersistenceAdapter(SpringDataExpedienteTesisRepository expedienteRepository,
                                        SpringDataTesisRepository tesisRepository,
                                        ExpedientePersistenceMapper mapper) {
        this.expedienteRepository = expedienteRepository;
        this.tesisRepository = tesisRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ExpedienteTesis guardar(ExpedienteTesis expediente) {
        ExpedienteTesisEntity entity = mapper.toEntity(expediente);
        ExpedienteTesisEntity saved = expedienteRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existeNumeroExpedienteActivo(String numeroExpediente) {
        return expedienteRepository.existsByNumeroExpedienteAndEstadoAndEliminadoFalse(numeroExpediente, 1);
    }
}
