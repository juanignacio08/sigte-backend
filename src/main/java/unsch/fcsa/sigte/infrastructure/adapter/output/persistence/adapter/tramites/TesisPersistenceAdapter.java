package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.tramites;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.tramites.TesisFiltro;
import unsch.fcsa.sigte.application.port.out.tramites.TesisRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.TesisEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites.TesisPersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataExpedienteTesisRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataTesisAutorRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.SpringDataTesisRepository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.tramites.spec.TesisSpecification;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TesisPersistenceAdapter implements TesisRepositoryPort {

    private final SpringDataTesisRepository tesisRepository;
    private final SpringDataTesisAutorRepository tesisAutorRepository;
    private final SpringDataExpedienteTesisRepository expedienteTesisRepository;
    private final TesisPersistenceMapper mapper;

    @Override
    public Tesis guardar(Tesis tesis) {
        TesisEntity entity = mapper.toEntity(tesis);
        TesisEntity saved = tesisRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public TesisDetalleView obtenerDetallePorId(UUID id) {
        return tesisRepository.findByIdAndEliminadoFalse(id)
                .map(mapper::toTesisDetalleView)
                .orElse(null);
    }

    @Override
    public PageResult<TesisDetalleView> listarPorFiltros(TesisFiltro filtro, int page, int size) {
        Specification<TesisEntity> spec = TesisSpecification.conFiltro(filtro);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaRegistro"));

        Page<TesisEntity> tesisPage = tesisRepository.findAll(spec, pageable);

        List<TesisDetalleView> list = tesisPage.stream()
                .map(mapper::toTesisDetalleView)
                .toList();

        return new PageResult<>(
                list,
                tesisPage.getNumber(),
                tesisPage.getSize(),
                tesisPage.getTotalElements(),
                tesisPage.getTotalPages(),
                tesisPage.isFirst(),
                tesisPage.isLast()
        );
    }

    @Override
    public boolean existeExpedientePorNumero(String numeroExpediente) {
        return expedienteTesisRepository.existsByNumeroExpedienteAndEliminadoFalse(numeroExpediente);
    }

    @Override
    public boolean estudianteTieneTesisActiva(Long estudianteId) {
        return tesisAutorRepository.existsByEstudiante_IdAndEliminadoFalse(estudianteId);
    }
}
