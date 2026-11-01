package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.tramites;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarDocumentosTesisUseCase;
import unsch.fcsa.sigte.domain.model.tramites.DocumentoTesis;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.DocumentoTesisResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.GenerarMemoRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.tramites.DocumentoTesisRestMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/documento")
public class DocumentoTesisController {

    private final GestionarDocumentosTesisUseCase useCase;
    private final DocumentoTesisRestMapper mapper;

    public DocumentoTesisController(GestionarDocumentosTesisUseCase useCase,
                                    DocumentoTesisRestMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @GetMapping("/expediente/{expedienteId}")
    public ResponseEntity<ResponseDTO<Object>> listarPorExpediente(@PathVariable Long expedienteId) {
        DocumentoTesisResponse response = mapper.toResponse(useCase.obtenerPorExpediente(expedienteId));

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                response, "Documentos del expediente", "200");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/tesis/{tesisId}")
    public ResponseEntity<ResponseDTO<Object>> listarPorTesis(@PathVariable UUID tesisId) {
        List<DocumentoTesisResponse> lista = useCase.listarPorTesis(tesisId)
                .stream()
                .map(mapper::toResponse)
                .toList();

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                lista, "Documentos de la tesis", "200");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/expediente/{expedienteId}/tipo/{tipoCodigo}")
    public ResponseEntity<ResponseDTO<Object>> listarPorExpedienteYTipo(
            @PathVariable Long expedienteId,
            @PathVariable String tipoCodigo) {

        List<DocumentoTesisResponse> lista = useCase.listarPorExpedienteYTipo(expedienteId, tipoCodigo)
                .stream()
                .map(mapper::toResponse)
                .toList();

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                lista, "Documentos del expediente por tipo", "200");
        return ResponseEntity.ok(body);
    }

    @PostMapping("/memo")
    public ResponseEntity<ResponseDTO<Object>> generarMemo(@Valid @RequestBody GenerarMemoRequest request) {

        DocumentoTesis nuevoDocumento = useCase.generarMemo(mapper.toCommand(request));

        DocumentoTesisResponse response = mapper.toResponse(nuevoDocumento);

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                response, "Memorando generado correctamente", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/memo/{documentoId}/regenerar")
    public ResponseEntity<ResponseDTO<Object>> volverAGenerar(@PathVariable Long documentoId) {
        DocumentoTesis nuevoDocumento = useCase.volverAGenerar(documentoId);
        DocumentoTesisResponse response = mapper.toResponse(nuevoDocumento);
        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                response, "Memorando generado correctamente", "201"
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @DeleteMapping("/{documentoId}")
    public ResponseEntity<ResponseDTO<Object>> eliminar(@PathVariable Long documentoId) {
        useCase.eliminarDocumento(documentoId);
        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                null, "Documento eliminado correctamente", "200");
        return ResponseEntity.ok(body);
    }
}
