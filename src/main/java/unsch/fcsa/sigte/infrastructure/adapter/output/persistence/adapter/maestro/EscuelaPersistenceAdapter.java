package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.maestro.projections.EscuelaProfesionalProjection;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.application.port.out.maestro.EscuelaProfesionalRepositoryPort;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro.EscuelaProfesionalMapperOut;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro.SpringDataEscuelaRepository;

import java.util.List;
import java.util.Optional;

@Component
public class EscuelaPersistenceAdapter implements EscuelaProfesionalRepositoryPort {

    private final SpringDataEscuelaRepository repository;

    private final EscuelaProfesionalMapperOut mapper;

    public EscuelaPersistenceAdapter(SpringDataEscuelaRepository repository, EscuelaProfesionalMapperOut mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public EscuelaProfesional guardar(EscuelaProfesional escuelaProfesional) {
        EscuelaEntity escuelaEntity = mapper.toEntity(escuelaProfesional);
        escuelaEntity = repository.save(escuelaEntity);
        return mapper.toDomain(escuelaEntity);
    }

    @Override
    public Optional<EscuelaProfesional> buscarPorId(Long id) {
        return repository.findById(id)
                .filter(entity -> !entity.getEliminado())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<EscuelaProfesionalProjection> obtenerDetallerPorId(Long id) {
        return repository.getDetailByID(id);
    }

    @Override
    public List<EscuelaProfesionalProjection> listarTodosPorFacultad(Long facultadId) {
        return repository.getDetailByFacultyId(facultadId);
    }

    @Override
    public List<EscuelaProfesional> listarPorDepartamento(Long departamentoId) {
        return repository.findByDepartamento_IdAndEliminadoFalse(departamentoId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorAbreviatura(String abreviatura) {
        return repository.existsByAbreviaturaAndEliminadoFalse(abreviatura);
    }

    @Override
    public boolean existePorCodigo(Integer codigo) {
        return repository.existsByCodigo(codigo);
    }

}
