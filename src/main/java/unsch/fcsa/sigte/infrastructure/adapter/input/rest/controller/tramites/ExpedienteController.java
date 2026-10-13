package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.tramites;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarExpedienteUseCase;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.ExpedienteResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.RegistrarExpedienteRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.file.MultipartFileMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.tramites.ExpedienteRestMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/expediente")
public class ExpedienteController {

    private final GestionarExpedienteUseCase gestionarExpedienteUseCase;
    private final ExpedienteRestMapper expedienteRestMapper;
    private final MultipartFileMapper multipartFileMapper;

    public ExpedienteController(GestionarExpedienteUseCase gestionarExpedienteUseCase, ExpedienteRestMapper restMapper, MultipartFileMapper multipartFileMapper) {
        this.gestionarExpedienteUseCase = gestionarExpedienteUseCase;
        this.expedienteRestMapper = restMapper;
        this.multipartFileMapper = multipartFileMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<Object>> registrarExpediente(
            @Valid @ModelAttribute RegistrarExpedienteRequest request
    ) {

        List<ArchivoSubida> archivos = multipartFileMapper.toArchivosSubida(request.archivos());

        ExpedienteTesis expedienteTesis = gestionarExpedienteUseCase.registrar(expedienteRestMapper.toCreate(request), archivos);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(
                expedienteRestMapper.toResponse(expedienteTesis),
                "Expediente de tesis registrado correctamente",
                "201"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/listarPorTesis/{id}")
    public ResponseEntity<ResponseDTO<Object>> obtenerListaPorTesis(@PathVariable UUID id) {
        List<ExpedienteResponse> list = gestionarExpedienteUseCase.listarPorTesis(id).stream()
                .map(expedienteRestMapper::toResponseWithMasterTable)
                .toList();

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(list, "Lista de expedientes de una tesis", "200");
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> obtenerPorId(@PathVariable Long id) {
        ExpedienteDetalleView domain = gestionarExpedienteUseCase.obtenerPorId(id);
        ExpedienteResponse response = expedienteRestMapper.toResponseDetail(domain);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(response, "Expediente a detalle", "200");
        return ResponseEntity.ok(responseDTO);
    }

}
