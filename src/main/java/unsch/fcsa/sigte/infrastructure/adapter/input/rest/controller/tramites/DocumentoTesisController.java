package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.tramites;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarDocumentosTesisUseCase;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.DocumentoTesisResponse;
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
        List<DocumentoTesisResponse> lista = useCase.listarPorExpediente(expedienteId)
                .stream()
                .map(mapper::toResponse)
                .toList();

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                lista, "Documentos del expediente", "200");
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
}
