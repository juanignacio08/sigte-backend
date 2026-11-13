package unsch.fcsa.sigte.application.port.in.tramites;

import unsch.fcsa.sigte.application.command.tramites.GenerarMemoCommand;
import unsch.fcsa.sigte.application.command.tramites.GenerarResolucionAprobacionProyectoCommand;
import unsch.fcsa.sigte.application.command.tramites.RegistrarDictamenCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ContenidoEmailMemo;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoTesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;

import java.util.List;
import java.util.UUID;

public interface GestionarDocumentosTesisUseCase {

    List<DocumentoTesisDetalleView> listarPorTesis(UUID tesisId);
    List<DocumentoTesisDetalleView> listarPorExpedienteYTipo(Long expedienteId, String tipoDocumentoCodigo);
    DocumentoTesis obtenerPorId(Long id);

    DocumentoTesis generarMemo(GenerarMemoCommand command);
    DocumentoTesis volverAGenerar(Long documentoId);
    DocumentoTesisDetalleView obtenerMemoPorExpediente(Long expedienteId);
    void eliminarMemo(Long documentoId);

    DocumentoTesis generarResolucionAprobacionProyecto(GenerarResolucionAprobacionProyectoCommand command);
    DocumentoTesis volverAGenerarResolucionAprobacionProyecto(Long documentoId);
    DocumentoTesisDetalleView obtenerResolucionPorExpediente(Long expedienteId);
    void eliminarResolucionAprobacionProyecto(Long documentoId);

    DocumentoTesis registrarDictamen(RegistrarDictamenCommand command);
    DocumentoTesisDetalleView obtenerDictamenPorExpediente(Long expedienteId);
    void eliminarDictamen(Long expedienteId);

    void enviarMemo(Long documentoId);
    ContenidoEmailMemo obtenerContenidoMemoAEnviar(Long expedienteId);

    void enviarResolucionProyecto(Long documentoId);
    ContenidoEmailMemo obtenerContenidoResolucionProyectoAEnviar(Long documentoId);
}
