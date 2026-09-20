package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.domain.port.out.maestro.DepartamentoRepositoryPort;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro.SpringDataDepartamentoRepository;

import java.util.List;
import java.util.Optional;

@Component
public class DepartamentoPersistenceAdapter implements DepartamentoRepositoryPort {

    private final SpringDataDepartamentoRepository repository;

    public DepartamentoPersistenceAdapter(SpringDataDepartamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public DepartamentoAcademico guardar(DepartamentoAcademico domain) {
        // Mapeo: Dominio -> Entidad JPA
        DepartamentoEntity entity = new DepartamentoEntity(
                domain.getId(),
                domain.getFacultadId(),
                domain.getAbreviatura(),
                domain.getDenominacion(),
                domain.getEstado(),
                domain.isEliminado()
        );

        DepartamentoEntity saved = repository.save(entity);

        // Mapeo: Entidad JPA -> Dominio
        return toDomain(saved);
    }

    @Override
    public Optional<DepartamentoAcademico> buscarPorId(Long id) {
        return repository.findById(id)
                .filter(entity -> !entity.getEliminado()) // Ignorar si está eliminado lógicamente
                .map(this::toDomain);
    }

    @Override
    public List<DepartamentoAcademico> listarTodos() {
        return repository.findByEliminadoFalse().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existePorAbreviatura(String abreviatura) {
        return repository.existsByAbreviaturaAndEliminadoFalse(abreviatura);
    }

    @Override
    public boolean existePorId(Long id) {
        return repository.existsByIdAndEliminadoFalse(id);
    }

    // Método helper para transformar JPA -> Dominio
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
