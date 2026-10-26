package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.ComisionRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Comision;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ComisionEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites.ComisionPersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataComisionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ComisionPersistenceAdapter implements ComisionRepositoryPort {

    private final SpringDataComisionRepository repository;
    private final ComisionPersistenceMapper mapper;
    private final EntityManager em;

    @Override
    public Comision guardar(Comision comision) {
        TesisEntity tesisRef = em.getReference(TesisEntity.class, comision.getTesisId());
        ComisionEntity entity = mapper.toEntity(comision);
        entity.setTesis(tesisRef);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<ComisionDetalleView> buscarPorId(Long id) {
        return repository.findByIdAndEliminadoFalse(id).map(mapper::toView);
    }

    @Override
    public Optional<ComisionDetalleView> obtenerPorTesisYEtapa(UUID tesisId, String etapa) {
        return repository.findByTesis_IdAndEtapaCodigoAndEliminadoFalse(tesisId, etapa)
                .map(mapper::toView);
    }

    @Override
    public List<ComisionDetalleView> listarPorTesis(UUID tesisId) {
        return repository.findAllByTesis_IdAndEliminadoFalse(tesisId).stream()
                .map(mapper::toView)
                .toList();
    }

    @Override
    public boolean existeComisionActivaPorTesisYEtapa(UUID tesisId, String etapaCodigo) {
        return repository.existsByTesis_IdAndEtapaCodigoAndEliminadoFalse(tesisId, etapaCodigo);
    }

    @Override
    public boolean existeComisionActiva(Long comisionId) {
        return repository.existsByIdAndEstadoAndEliminadoFalse(comisionId, 1);
    }
}
