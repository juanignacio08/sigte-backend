package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.personas;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.port.in.personas.GestionarEstudianteUseCase;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteDetalleView;
import unsch.fcsa.sigte.application.command.personas.EstudianteFiltro;
import unsch.fcsa.sigte.domain.model.personas.Estudiante;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.PaginatedResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.EstudianteRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.EstudianteResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.personas.EstudianteRestMapper;

import java.util.List;

@RestController
@RequestMapping("/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final GestionarEstudianteUseCase gestionarEstudianteUseCase;
    private final EstudianteRestMapper restMapper;

    @PostMapping
    public ResponseEntity<ResponseDTO<Object>> guardar(@RequestBody @Valid EstudianteRequest request) {
        Estudiante estudiante = gestionarEstudianteUseCase.guardar(restMapper.toCommandCreate(request));
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(estudiante), "Estudiante registrado!", "201");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> editar(@PathVariable Long id, @RequestBody @Valid EstudianteRequest request) {
        Estudiante estudiante = gestionarEstudianteUseCase.actualizar(restMapper.toCommandUpdate(id, request));
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(estudiante), "Estudiante actualizado!", "201");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> getById(@PathVariable Long id) {
        EstudianteDetalleView detalle = gestionarEstudianteUseCase.obtenerDetallePorId(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.fromViewToResponse(detalle), "Estudiante obtenido!", "200");
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/filtrar")
    public ResponseEntity<PaginatedResponseDTO> getByFilters(
            @RequestBody EstudianteFiltro filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        PageResult<EstudianteDetalleView> result = gestionarEstudianteUseCase.listarPorFiltros(filtro, page, size);

        List<EstudianteResponse> list = result.getContent().stream()
                .map(restMapper::fromViewToResponse)
                .toList();

        return ResponseEntity.ok(ResponseGlobalMapper.buildPaginated(result, list, "Listado de Estudiantes"));
    }

    @PutMapping("/activar/{id}")
    public ResponseEntity<ResponseDTO<Object>> activar(@PathVariable Long id) {
        Estudiante estudiante = gestionarEstudianteUseCase.activar(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(estudiante), "Estudiante activado!", "201");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/desactivar/{id}")
    public ResponseEntity<ResponseDTO<Object>> desactivar(@PathVariable Long id) {
        Estudiante estudiante = gestionarEstudianteUseCase.desactivar(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(restMapper.toResponse(estudiante), "Estudiante desactivado!", "201");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> eliminar(@PathVariable Long id) {
        gestionarEstudianteUseCase.eliminar(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(null, "Estudiante eliminado!", "200");
        return ResponseEntity.ok().body(responseDTO);
    }
}