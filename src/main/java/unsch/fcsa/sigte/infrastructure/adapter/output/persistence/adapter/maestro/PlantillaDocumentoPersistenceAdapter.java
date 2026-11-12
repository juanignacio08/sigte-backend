package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.port.out.maestro.PlantillaDocumentoRepositoryPort;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.PlantillaDocumentoDetalleView;
import unsch.fcsa.sigte.domain.model.maestro.PlantillaDocumento;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro.PlantillaDocumentoPersistenceMapper;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro.SpringDataPlantillaDocumentoRepository;

import java.util.List;
import java.util.Optional;

@Component
public class PlantillaDocumentoPersistenceAdapter implements PlantillaDocumentoRepositoryPort {

    private final SpringDataPlantillaDocumentoRepository repository;
    private final PlantillaDocumentoPersistenceMapper mapper;

    public PlantillaDocumentoPersistenceAdapter(
            SpringDataPlantillaDocumentoRepository repository,
            PlantillaDocumentoPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public PlantillaDocumento guardar(PlantillaDocumento plantilla) {
        return mapper.toDomain(repository.save(mapper.toEntity(plantilla)));
    }

    @Override
    public Optional<PlantillaDocumento> buscarPorId(Long id) {
        return repository.findByIdAndEliminadoFalse(id).map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlantillaDocumentoDetalleView> buscarADetallePlantillaActivaPorId(Long id) {
        return repository.findByIdAndEstadoAndEliminadoFalse(id, 1).map(mapper::toDetalleView);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlantillaDocumentoDetalleView> obtenerPlantillaActivaPorEscuelaYTipo(Long escuelaId, String tipoDocumentoCodigo) {
        return repository
                .findByEscuelaIdAndTipoDocumentoCodigoAndEstadoAndEliminadoFalse(escuelaId, tipoDocumentoCodigo, 1)
                .map(mapper::toDetalleView);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlantillaDocumentoDetalleView> listarPorEscuela(Long escuelaId) {
        return repository.findByEscuelaIdAndEliminadoFalse(escuelaId)
                .stream()
                .map(mapper::toDetalleView)
                .toList();
    }

    @Override
    public boolean existePlantillaActivaPorEscuelaYTipo(Long escuelaId, String tipoDocumentoCodigo) {
        return repository.existsByEscuelaIdAndTipoDocumentoCodigoAndEstadoAndEliminadoFalse(
                escuelaId, tipoDocumentoCodigo, 1);
    }
}