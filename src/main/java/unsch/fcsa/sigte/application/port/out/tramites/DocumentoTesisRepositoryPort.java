package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoTesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentoTesisRepositoryPort {
    DocumentoTesis guardar(DocumentoTesis documento);
    Optional<DocumentoTesis> buscarPorId(Long id);
    DocumentoTesisDetalleView listarPorExpediente(Long expedienteId);
    List<DocumentoTesisDetalleView> listarPorTesis(UUID tesisId);
    List<DocumentoTesisDetalleView> listarPorExpedienteYTipo(Long expedienteId, String tipoDocumentoCodigo);
    boolean existeCorrelativo(String tipoDocumentoCodigo, Integer anio, Integer numero);
    boolean existeMemoGeneradoPorExpediente(Long expedienteId);
    void eliminar(Long id);
}
