package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.port.out.tramites.EnvioCorreoRepositoryPort;
import unsch.fcsa.sigte.domain.model.tramites.EnvioCorreo;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.DocumentoTesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.EnvioCorreoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites.EnvioCorreoPersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataEnvioCorreoRepository;

import java.util.List;
import java.util.Optional;

@Component
public class EnvioCorreoPersistenceAdapter implements EnvioCorreoRepositoryPort {

    private final SpringDataEnvioCorreoRepository repository;
    private final EnvioCorreoPersistenceMapper mapper;
    private final EntityManager em;

    public EnvioCorreoPersistenceAdapter(SpringDataEnvioCorreoRepository repository,
                                         EnvioCorreoPersistenceMapper mapper,
                                         EntityManager em) {
        this.repository = repository;
        this.mapper = mapper;
        this.em = em;
    }

    @Override
    @Transactional
    public EnvioCorreo guardar(EnvioCorreo envio) {
        DocumentoTesisEntity documentoTesisEntity = em.getReference(DocumentoTesisEntity.class, envio.getDocumentoId());
        EnvioCorreoEntity entity = mapper.toEntity(envio);
        entity.setDocumento(documentoTesisEntity);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnvioCorreo> buscarPorId(Long id) {
        return repository.findByIdAndEliminadoFalse(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnvioCorreo> listarPorDocumento(Long documentoId) {
        return repository.findByDocumento_IdAndEliminadoFalseOrderByFechaEnvioDesc(documentoId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existeEnvioExitosoPorDocumento(Long documentoId) {
        return repository.existsByDocumento_IdAndEstadoAndEliminadoFalse(documentoId, 2);
    }
}