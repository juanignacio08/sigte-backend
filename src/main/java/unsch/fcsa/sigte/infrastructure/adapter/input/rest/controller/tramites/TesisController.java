package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.tramites;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.TesisFiltro;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarTesisUseCase;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.PaginatedResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.RegistrarTesisRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.TesisResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.file.MultipartFileMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.tramites.TesisRestMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tesis")
@RequiredArgsConstructor
public class TesisController {

    private final GestionarTesisUseCase gestionarTesisUseCase;
    private final TesisRestMapper restMapper;
    private final MultipartFileMapper multipartFileMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<Object>> registrar(
            @ModelAttribute @Valid RegistrarTesisRequest request
    ) {
        List<ArchivoSubida> archivos = multipartFileMapper.toArchivosSubida(request.archivosPdfs());

        Tesis tesis = gestionarTesisUseCase.registrarProyecto(
                restMapper.toCommandCreate(request),
                archivos
        );

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(
                restMapper.toResponse(tesis),
                "Proyecto de tesis registrado correctamente",
                "201"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> obtenerPorId(@PathVariable UUID id) {
        TesisDetalleView detalle = gestionarTesisUseCase.obtenerPorId(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponseDetail(detalle), "Detalle de tesis obtenido correctamente", "200");
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/filtrar")
    public ResponseEntity<PaginatedResponseDTO> listarPorFiltros(
            @RequestBody TesisFiltro filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        PageResult<TesisDetalleView> result = gestionarTesisUseCase.listarPorFiltros(filtro, page, size);

        List<TesisResponse> list = result.content().stream()
                .map(restMapper::toResponseDetail)
                .toList();

        return ResponseEntity.ok(ResponseGlobalMapper.buildPaginated(result, list, "Listado de Tesis"));
    }
}
