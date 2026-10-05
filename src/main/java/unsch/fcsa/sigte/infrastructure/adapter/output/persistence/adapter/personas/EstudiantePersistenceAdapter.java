package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.personas;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.port.out.personas.EstudianteRepositoryPort;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteDetalleView;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteFiltro;
import unsch.fcsa.sigte.domain.model.personas.Estudiante;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.EstudianteEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.personas.EstudiantePersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas.SpringDataEstudianteRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.personas.spec.EstudianteSpecification;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EstudiantePersistenceAdapter implements EstudianteRepositoryPort {

    private final SpringDataEstudianteRepository estudianteRepository;
    private final EstudiantePersistenceMapper mapper;

    @Override
    public Estudiante guardar(Estudiante estudiante) {
        EstudianteEntity entity = mapper.toEntity(estudiante);
        EstudianteEntity saved = estudianteRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Estudiante> obtenerPorId(Long id) {
        return estudianteRepository.findByIdEqualsAndEliminadoFalse(id)
                .map(mapper::toDomain);
    }

    @Override
    public EstudianteDetalleView obtenerDetallePorId(Long id) {
        return estudianteRepository.findByIdAndEliminadoFalse(id)
                .map(mapper::toEstudianteDetalleView)
                .orElse(null);
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        return estudianteRepository.existsByCodigoAndEliminadoFalse(codigo);
    }

    @Override
    public boolean existePorNumeroDocumento(String numeroDocumento) {
        return estudianteRepository.existsByNumeroDocumentoAndEliminadoFalse(numeroDocumento);
    }

    @Override
    public boolean existePorCorreoInstitucional(String correo) {
        return estudianteRepository.existsByCorreoInstitucional(correo);
    }

    @Override
    public PageResult<EstudianteDetalleView> listarPorFiltros(EstudianteFiltro filtro, int page, int size) {
        Specification<EstudianteEntity> spec = EstudianteSpecification.conFiltro(filtro);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "primerApellido"));

        Page<EstudianteEntity> estudiantePage = estudianteRepository.findAll(spec, pageable);

        List<EstudianteDetalleView> list = estudiantePage.stream()
                .map(mapper::toEstudianteDetalleView)
                .toList();

        return new PageResult<>(
                list,
                estudiantePage.getNumber(),
                estudiantePage.getSize(),
                estudiantePage.getTotalElements(),
                estudiantePage.getTotalPages(),
                estudiantePage.isFirst(),
                estudiantePage.isLast()
        );
    }
}