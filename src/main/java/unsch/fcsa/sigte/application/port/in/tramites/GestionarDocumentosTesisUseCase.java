package unsch.fcsa.sigte.application.port.in.tramites;

import unsch.fcsa.sigte.application.command.tramites.GenerarMemoCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoTesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;

import java.util.List;
import java.util.UUID;

public interface GestionarDocumentosTesisUseCase {

    List<DocumentoTesisDetalleView> listarPorExpediente(Long expedienteId);
    List<DocumentoTesisDetalleView> listarPorTesis(UUID tesisId);
    List<DocumentoTesisDetalleView> listarPorExpedienteYTipo(Long expedienteId, String tipoDocumentoCodigo);
    DocumentoTesis obtenerPorId(Long id);

    DocumentoTesis generarMemo(GenerarMemoCommand command);
}
