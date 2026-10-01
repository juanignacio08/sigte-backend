package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.personas;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.personas.DocenteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteFiltro;
import unsch.fcsa.sigte.domain.model.personas.Docente;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.DocenteEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.personas.DocentePersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas.SpringDataDocenteRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DocentePersistenceAdapter implements DocenteRepositoryPort {

    private final SpringDataDocenteRepository docenteRepository;

    private final DocentePersistenceMapper mapper;

    @Override
    public Docente guardar(Docente docente) {
        DocenteEntity entity = mapper.toEntity(docente);
        DocenteEntity saved = docenteRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Docente> obtenerPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean existePorCorreoInstitucional(String correo) {
        return false;
    }

    @Override
    public boolean existePorCorreoInstitucionalYIdDistinto(String correo, Long id) {
        return false;
    }

    @Override
    public boolean existePorDocumento(String tipoDocumentoCodigo, String numeroDocumento) {
        return docenteRepository.existsByTipoDocumentoCodigoAndNumeroDocumento(tipoDocumentoCodigo, numeroDocumento);
    }

    @Override
    public boolean existePorDocumentoYIdDistinto(String tipoDocumentoCodigo, String numeroDocumento, Long id) {
        return false;
    }

    @Override
    public long contarPaginado(DocenteFiltro filtro) {
        return 0;
    }
}
