package unsch.fcsa.sigte.application.port.out.maestro;

import unsch.fcsa.sigte.application.port.out.maestro.dtos.PlantillaDocumentoDetalleView;
import unsch.fcsa.sigte.domain.model.maestro.PlantillaDocumento;

import java.util.List;
import java.util.Optional;

public interface PlantillaDocumentoRepositoryPort {
    PlantillaDocumento guardar(PlantillaDocumento plantilla);
    Optional<PlantillaDocumento> buscarPorId(Long id);
    Optional<PlantillaDocumentoDetalleView> buscarADetallePlantillaActivaPorId(Long id);
    Optional<PlantillaDocumentoDetalleView> obtenerPlantillaActivaPorEscuelaYTipo(Long escuelaId, String tipoDocumentoCodigo);
    List<PlantillaDocumentoDetalleView> listarPorEscuela(Long escuelaId);
    boolean existePlantillaActivaPorEscuelaYTipo(Long escuelaId, String tipoDocumentoCodigo);
}