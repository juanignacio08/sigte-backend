package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.domain.port.out.maestro.EscuelaProfesionalRepositoryPort;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro.SpringDataEscuelaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class EscuelaPersistenceAdapter implements EscuelaProfesionalRepositoryPort {

    private final SpringDataEscuelaRepository repository;

    public EscuelaPersistenceAdapter(SpringDataEscuelaRepository repository) {
        this.repository = repository;
    }

    @Override
    public EscuelaProfesional guardar(EscuelaProfesional escuelaProfesional) {
        EscuelaEntity escuelaEntity = toEntity(escuelaProfesional);
        escuelaEntity = repository.save(escuelaEntity);
        return toDomain(escuelaEntity);
    }

    @Override
    public Optional<EscuelaProfesional> buscarPorId(Long id) {
        return repository.findById(id)
                .filter(entity -> !entity.getEliminado())
                .map(this::toDomain);
    }

    @Override
    public Optional<EscuelaProfesional> buscarPorCodigo(Integer codigo) {
        return Optional.empty();
    }

    @Override
    public List<EscuelaProfesional> listarTodos() {
        return repository.findByEliminadoFalse().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public boolean existePorAbreviatura(String abreviatura) {
        return repository.existsByAbreviaturaAndEliminadoFalse(abreviatura);
    }

    @Override
    public boolean existePorCodigo(Integer codigo) {
        return repository.existsByCodigoAndEliminadoFalse(codigo);
    }

    private EscuelaEntity toEntity(EscuelaProfesional escuelaProfesional) {
        DepartamentoEntity departamentoEntity = new DepartamentoEntity();
        departamentoEntity.setId(escuelaProfesional.getDepartamentoAcademicoId());

        return new EscuelaEntity(
                escuelaProfesional.getId(),
                departamentoEntity,
                escuelaProfesional.getCodigo(),
                escuelaProfesional.getAbreviatura(),
                escuelaProfesional.getDenominacion(),
                escuelaProfesional.getPlantillaMemorando(),
                escuelaProfesional.getPlantillaResolucion(),
                escuelaProfesional.getEstado(),
                escuelaProfesional.isEliminado()
        );
    }

    private EscuelaProfesional toDomain(EscuelaEntity entity) {
        return new EscuelaProfesional(
                entity.getId(),
                entity.getDepartamento().getId(),
                entity.getCodigo(),
                entity.getAbreviatura(),
                entity.getDenominacion(),
                entity.getPlantillaMemorando(),
                entity.getPlantillaResolucion(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }
}
