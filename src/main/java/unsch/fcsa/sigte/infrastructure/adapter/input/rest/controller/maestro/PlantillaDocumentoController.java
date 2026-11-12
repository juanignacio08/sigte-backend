package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.maestro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarPlantillaDocumentoUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.PlantillaDocumentoDetalleView;
import unsch.fcsa.sigte.domain.model.maestro.PlantillaDocumento;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.ActualizarPlantillaDocumentoRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.PlantillaDocumentoResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.RegistrarPlantillaDocumentoRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro.PlantillaDocumentoRestMapper;

import java.util.List;

@RestController
@RequestMapping("/plantilla")
public class PlantillaDocumentoController {

    private final GestionarPlantillaDocumentoUseCase useCase;
    private final PlantillaDocumentoRestMapper mapper;

    public PlantillaDocumentoController(GestionarPlantillaDocumentoUseCase useCase,
                                        PlantillaDocumentoRestMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<Object>> registrar(
            @Valid @ModelAttribute RegistrarPlantillaDocumentoRequest request) {

        PlantillaDocumento plantilla = useCase.registrar(mapper.toCommand(request));

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                mapper.toResponse(plantilla),
                "Plantilla registrada correctamente",
                "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<Object>> actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute ActualizarPlantillaDocumentoRequest request) {

        PlantillaDocumento plantilla = useCase.actualizar(mapper.toCommand(id, request));

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                mapper.toResponse(plantilla),
                "Plantilla actualizada correctamente",
                "200");

        return ResponseEntity.ok(body);
    }

    @GetMapping("/escuela/{escuelaId}")
    public ResponseEntity<ResponseDTO<Object>> listarPorEscuela(@PathVariable Long escuelaId) {
        List<PlantillaDocumentoResponse> lista = useCase.listarPorEscuela(escuelaId)
                .stream()
                .map(mapper::toResponse)
                .toList();

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                lista,
                "Plantillas de la escuela",
                "200");

        return ResponseEntity.ok(body);
    }

    @GetMapping("/escuela/{escuelaId}/tipo/{tipoCodigo}")
    public ResponseEntity<ResponseDTO<Object>> obtenerPorEscuelaYTipo(
            @PathVariable Long escuelaId,
            @PathVariable String tipoCodigo) {

        PlantillaDocumentoDetalleView view = useCase.obtenerPorEscuelaYTipo(escuelaId, tipoCodigo);

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                mapper.toResponse(view),
                "Plantilla de la escuela",
                "200");

        return ResponseEntity.ok(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> eliminar(@PathVariable Long id) {
        useCase.eliminar(id);

        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                null,
                "Plantilla eliminada correctamente",
                "200");

        return ResponseEntity.ok(body);
    }
}
