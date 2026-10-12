package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.personas;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.port.in.personas.GestionarDocenteUseCase;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteDetalleView;
import unsch.fcsa.sigte.application.command.personas.DocenteFiltro;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteMetricasProjection;
import unsch.fcsa.sigte.domain.model.personas.Docente;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.PaginatedResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.DocenteRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.DocenteResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.personas.DocenteRestMapper;

import java.util.List;

@RestController
@RequestMapping("/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final GestionarDocenteUseCase gestionarDocenteUseCase;
    private final DocenteRestMapper restMapper;

    @PostMapping
    public ResponseEntity<ResponseDTO<Object>> guardar(@RequestBody @Valid DocenteRequest request) {
        Docente docente = gestionarDocenteUseCase.guardar(restMapper.toDocenteCommandCreate(request));
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(docente), "Docente registrado!", "201");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> editar(@PathVariable Long id, @RequestBody @Valid DocenteRequest request) {
        Docente docente = gestionarDocenteUseCase.actualizar(restMapper.toDocenteCommandUpdate(id, request));
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(docente), "Docente actualizado!", "201");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/filtrar")
    public ResponseEntity<PaginatedResponseDTO> getByFilters(
            @RequestBody DocenteFiltro filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        PageResult<DocenteDetalleView> result =
                gestionarDocenteUseCase.listarPorFiltros(filtro, page, size);

        List<DocenteResponse> list = result.content().stream()
                .map(restMapper::fromDocenteViewToResponse).toList();

        return ResponseEntity.ok(ResponseGlobalMapper.buildPaginated(result, list, "Listado de Docentes"));
    }

    @GetMapping("metricas/{facultadId}")
    public ResponseEntity<ResponseDTO<Object>> getMetricas(@PathVariable Long facultadId) {
        DocenteMetricasProjection projection = gestionarDocenteUseCase.obtenerMetricasPorFacultad(facultadId);
        return ResponseEntity.ok(ResponseGlobalMapper.build(restMapper.toMetrics(projection), "Metricas por Facultad", "200"));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseDTO<Object>> getById(@PathVariable Long id) {
        Docente domain = gestionarDocenteUseCase.obtenerPorId(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(domain), "Docente obtenido!", "200");
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("obtenerPorNumDocumento")
    public ResponseEntity<ResponseDTO<Object>> getById(
            @RequestParam String tipoDocumento,
            @RequestParam String numeroDocumento
    ) {
        Docente domain = gestionarDocenteUseCase.obtenerPorNumDocumento(tipoDocumento, numeroDocumento);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(domain), "Docente obtenido!", "200");
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("activar/{id}")
    public ResponseEntity<ResponseDTO<Object>> activar(@PathVariable Long id) {
        Docente dominio = gestionarDocenteUseCase.activar(id);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(dominio), "Docente activado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("desactivar/{id}")
    public ResponseEntity<ResponseDTO<Object>> desactivar(@PathVariable Long id) {
        Docente dominio = gestionarDocenteUseCase.desactivar(id);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(dominio), "Docente desactivado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDTO<Object>> eliminar(@PathVariable Long id) {
        gestionarDocenteUseCase.eliminar(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(null, "Docente eliminado!", "200");
        return ResponseEntity.ok().body(responseDTO);
    }
}
