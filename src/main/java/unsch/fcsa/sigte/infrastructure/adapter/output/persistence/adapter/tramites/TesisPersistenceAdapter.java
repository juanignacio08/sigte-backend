package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.TesisRepositoryPort;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites.TesisPersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataExpedienteTesisRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataTesisAutorRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataTesisRepository;

@Component
@RequiredArgsConstructor
public class TesisPersistenceAdapter implements TesisRepositoryPort {

    private final SpringDataTesisRepository tesisRepository;
    private final SpringDataTesisAutorRepository tesisAutorRepository;
    private final SpringDataExpedienteTesisRepository expedienteTesisRepository;
    private final TesisPersistenceMapper mapper;

    @Override
    public Tesis guardar(Tesis tesis) {
        TesisEntity entity = mapper.toEntity(tesis);
        TesisEntity saved = tesisRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existeExpedientePorNumero(String numeroExpediente) {
        return expedienteTesisRepository.existsByNumeroExpedienteAndEliminadoFalse(numeroExpediente);
    }

    @Override
    public boolean estudianteTieneTesisActiva(Long estudianteId) {
        return tesisAutorRepository.existsByEstudiante_IdAndEliminadoFalse(estudianteId);
    }
}
