package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.port.out.tramites.EnvioCorreoRepositoryPort;
import unsch.fcsa.sigte.domain.model.tramites.EnvioCorreo;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites.EnvioCorreoPersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataEnvioCorreoRepository;

import java.util.List;
import java.util.Optional;

@Component
public class EnvioCorreoPersistenceAdapter implements EnvioCorreoRepositoryPort {

    private final SpringDataEnvioCorreoRepository repository;
    private final EnvioCorreoPersistenceMapper mapper;

    public EnvioCorreoPersistenceAdapter(SpringDataEnvioCorreoRepository repository,
                                         EnvioCorreoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public EnvioCorreo guardar(EnvioCorreo envio) {
        return mapper.toDomain(repository.save(mapper.toEntity(envio)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnvioCorreo> buscarPorId(Long id) {
        return repository.findByIdAndEliminadoFalse(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvioCorreo> listarPorExpediente(Long expedienteId) {
        return repository.findByExpedienteIdAndEliminadoFalseOrderByFechaEnvioDesc(expedienteId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existeEnvioExitosoPorExpediente(Long expedienteId) {
        return repository.existsByExpedienteIdAndEstadoAndEliminadoFalse(expedienteId, 2);
    }
}