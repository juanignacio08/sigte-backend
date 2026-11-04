package unsch.fcsa.sigte.application.port.in.tramites;

import unsch.fcsa.sigte.application.command.tramites.GenerarMemoCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ContenidoEmailMemo;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoTesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;

import java.util.List;
import java.util.UUID;

public interface GestionarDocumentosTesisUseCase {

    DocumentoTesisDetalleView obtenerPorExpediente(Long expedienteId);
    List<DocumentoTesisDetalleView> listarPorTesis(UUID tesisId);
    List<DocumentoTesisDetalleView> listarPorExpedienteYTipo(Long expedienteId, String tipoDocumentoCodigo);
    DocumentoTesis obtenerPorId(Long id);

    DocumentoTesis generarMemo(GenerarMemoCommand command);
    DocumentoTesis volverAGenerar(Long documentoId);
    void eliminarDocumento(Long documentoId);

    void enviarMemo(Long documentoId);
    ContenidoEmailMemo obtenerContenidoAEnviar(Long expedienteId);
}
