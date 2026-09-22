package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.maestro.FacultadRepositoryPort;
import unsch.fcsa.sigte.domain.model.maestro.Facultad;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro.FacultadMapperOut;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro.SpringDataFacultadRepository;

import java.util.Optional;

@Component
public class FacultadPersistenceAdapter implements FacultadRepositoryPort {

    private final SpringDataFacultadRepository repository;
    private final FacultadMapperOut mapper;

    public FacultadPersistenceAdapter(SpringDataFacultadRepository repository, FacultadMapperOut mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Facultad> buscarPorId(Long id) {
        return repository.findById(id)
                .filter(e -> !e.getEliminado())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existePorId(Long id) {
        return repository.existsByIdAndEliminadoFalse(id);
    }
}
