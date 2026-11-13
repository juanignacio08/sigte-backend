package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.ComisionMiembroRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionMiembroDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ComisionMiembro;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.DocenteEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ComisionMiembroEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites.ComisionMiembroPersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataComisionMiembroRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ComisionMIembroPersistenceAdapter implements ComisionMiembroRepositoryPort {

    private final SpringDataComisionMiembroRepository comisionMiembroRepository;
    private final EntityManager entityManager;
    private final ComisionMiembroPersistenceMapper mapper;

    @Override
    public ComisionMiembro guardar(ComisionMiembro miembro) {
        DocenteEntity docenteEntity = entityManager.getReference(DocenteEntity.class, miembro.getDocenteId());
        ComisionMiembroEntity entity = mapper.toEntity(miembro);
        entity.setDocente(docenteEntity);
        return mapper.toDomain(comisionMiembroRepository.save(entity));
    }

    @Override
    public List<ComisionMiembro> guardarTodo(List<ComisionMiembro> miembros) {
        List<ComisionMiembroEntity> entities = new ArrayList<>();
        for (ComisionMiembro miembro : miembros) {
            DocenteEntity docenteEntity = entityManager.getReference(DocenteEntity.class, miembro.getDocenteId());
            ComisionMiembroEntity entity = mapper.toEntity(miembro);
            entity.setDocente(docenteEntity);
            entities.add(entity);
        }
        return comisionMiembroRepository.saveAll(entities).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<ComisionMiembro> buscarPorId(Long id) {
        return comisionMiembroRepository.findByIdAndEliminadoFalse(id).map(mapper::toDomain);
    }

    @Override
    public List<ComisionMiembro> listarActivosPorComision(Long comisionId) {
        return comisionMiembroRepository.findByComisionIdAndEstadoAndEliminadoFalse(comisionId, 1).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ComisionMiembroDetalleView> listarDetallePorComision(Long comisionId) {
        return comisionMiembroRepository.findByComisionIdAndEstadoAndEliminadoFalseOrderByOrdenAsc(comisionId, 1).stream()
                .map(mapper::toDetalleView)
                .toList();
    }

    @Override
    public boolean existeDocenteActivoEnComision(Long comisionId, Long docenteId) {
        return comisionMiembroRepository.existsByComisionIdAndDocente_Id(comisionId, docenteId);
    }

    @Override
    public boolean existeRolSingularActivo(Long comisionId, String rolCodigo) {
        return comisionMiembroRepository.existsByComisionIdAndRolCodigo(comisionId, rolCodigo);
    }

    @Override
    public boolean existePorId(Long id) {
        return comisionMiembroRepository.existsById(id);
    }

    @Override
    public void eliminar(Long id) {
        comisionMiembroRepository.deleteById(id);
    }
}
