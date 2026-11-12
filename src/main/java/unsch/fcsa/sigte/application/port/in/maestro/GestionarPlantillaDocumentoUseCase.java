package unsch.fcsa.sigte.application.port.in.maestro;

import unsch.fcsa.sigte.application.command.maestro.ActualizarPlantillaCommand;
import unsch.fcsa.sigte.application.command.maestro.RegistrarPlantillaCommand;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.PlantillaDocumentoDetalleView;
import unsch.fcsa.sigte.domain.model.maestro.PlantillaDocumento;

import java.util.List;

public interface GestionarPlantillaDocumentoUseCase {
    PlantillaDocumento registrar(RegistrarPlantillaCommand command);
    PlantillaDocumento actualizar(ActualizarPlantillaCommand command);
    PlantillaDocumentoDetalleView obtenerPorId(Long id);
    PlantillaDocumentoDetalleView obtenerPorEscuelaYTipo(Long escuelaId, String tipoDocumentoCodigo);
    List<PlantillaDocumentoDetalleView> listarPorEscuela(Long escuelaId);
    PlantillaDocumento desactivar(Long id);
    PlantillaDocumento activar(Long id);
    void eliminar(Long id);
}