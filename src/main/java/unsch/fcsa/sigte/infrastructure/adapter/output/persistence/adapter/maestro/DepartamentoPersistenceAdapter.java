package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.maestro.projections.DepartamentoConTotalEscuelasView;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.application.port.out.maestro.DepartamentoRepositoryPort;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro.DepartamentoMapperOut;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro.SpringDataDepartamentoRepository;

import java.util.List;
import java.util.Optional;

@Component
public class DepartamentoPersistenceAdapter implements DepartamentoRepositoryPort {

    private final SpringDataDepartamentoRepository repository;

    private final DepartamentoMapperOut mapper;

    public DepartamentoPersistenceAdapter(SpringDataDepartamentoRepository repository, DepartamentoMapperOut mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DepartamentoAcademico guardar(DepartamentoAcademico domain) {
        DepartamentoEntity entity = mapper.toEntity(domain);
        DepartamentoEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<DepartamentoAcademico> buscarPorId(Long id) {
        return repository.findById(id)
                .filter(entity -> !entity.getEliminado()) // Ignorar si está eliminado lógicamente
                .map(this::toDomain);
    }

    @Override
    public List<DepartamentoConTotalEscuelasView> listarTodosPorFacultad(Long facultadId) {
        return repository.listarConTotalEscuelasPorFacultad(facultadId);
    }

    @Override
    public boolean existePorAbreviatura(String abreviatura) {
        return repository.existsByAbreviaturaAndEliminadoFalse(abreviatura);
    }

    @Override
    public boolean existePorId(Long id) {
        return repository.existsByIdAndEliminadoFalse(id);
    }

    private DepartamentoAcademico toDomain(DepartamentoEntity entity) {
        return new DepartamentoAcademico(
                entity.getId(),
                entity.getFacultadId(),
                entity.getAbreviatura(),
                entity.getDenominacion(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }
}
