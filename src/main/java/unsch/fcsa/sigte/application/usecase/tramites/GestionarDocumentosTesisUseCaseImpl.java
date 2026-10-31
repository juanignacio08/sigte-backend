package unsch.fcsa.sigte.application.usecase.tramites;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarDocumentosTesisUseCase;
import unsch.fcsa.sigte.application.port.out.tramites.DocumentoTesisRepositoryPort;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoTesisDetalleView;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;

import java.util.List;
import java.util.UUID;

@Slf4j
public class GestionarDocumentosTesisUseCaseImpl implements GestionarDocumentosTesisUseCase {

    private final DocumentoTesisRepositoryPort repositoryPort;

    public GestionarDocumentosTesisUseCaseImpl(DocumentoTesisRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoTesisDetalleView> listarPorExpediente(Long expedienteId) {
        log.info("Listando documentos del expediente. [ EXPEDIENTE : {} ]", expedienteId);
        return repositoryPort.listarPorExpediente(expedienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoTesisDetalleView> listarPorTesis(UUID tesisId) {
        log.info("Listando documentos de la tesis. [ TESIS : {} ]", tesisId);
        return repositoryPort.listarPorTesis(tesisId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentoTesisDetalleView> listarPorExpedienteYTipo(Long expedienteId, String tipoDocumentoCodigo) {
        log.info("Listando documentos del expediente por tipo. [ EXPEDIENTE : {} | TIPO : {} ]",
                expedienteId, tipoDocumentoCodigo);
        return repositoryPort.listarPorExpedienteYTipo(expedienteId, tipoDocumentoCodigo);
    }

    @Override
    public DocumentoTesis obtenerPorId(Long id) {
        log.info("Obteniendo documento por id. [ DOCUMENTO : {} ]", id);
        return repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "DocumentoTesis", "id", id.toString()));
    }
}
