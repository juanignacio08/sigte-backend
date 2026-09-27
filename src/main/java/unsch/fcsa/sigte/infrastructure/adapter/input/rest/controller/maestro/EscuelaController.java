package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.maestro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.command.maestro.EscuelaCommand;
import unsch.fcsa.sigte.application.port.out.maestro.projections.EscuelaProfesionalProjection;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.DepartamentoResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalResponse;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarEscuelaUseCase;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro.EscuelaProfesionalMapperExpose;

import java.util.List;

@RestController
@RequestMapping("/escuelas")
public class EscuelaController {

    private final GestionarEscuelaUseCase gestionarEscuelaUseCase;

    private final EscuelaProfesionalMapperExpose mapper;

    public EscuelaController(GestionarEscuelaUseCase gestionarEscuelaUseCase, EscuelaProfesionalMapperExpose mapper) {
        this.gestionarEscuelaUseCase = gestionarEscuelaUseCase;
        this.mapper = mapper;
    }

    @PostMapping("guardar")
    public ResponseEntity<ResponseDTO<Object>> crear(@RequestBody @Valid EscuelaProfesionalRequest request) {
        EscuelaCommand command = EscuelaCommand.create(
                request.departamentoId(),
                request.codigo(),
                request.abreviatura(),
                request.denominacion(),
                request.plantillaMemo(),
                request.plantillaRes()
        );

        EscuelaProfesional creado = gestionarEscuelaUseCase.crearEscuela(command);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(creado), "Escuela Profesional registrado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("modificar")
    public ResponseEntity<ResponseDTO<Object>> modificar(@RequestBody @Valid EscuelaProfesionalRequest request) {
        EscuelaCommand command = EscuelaCommand.update(
                request.id(),
                request.departamentoId(),
                request.codigo(),
                request.abreviatura(),
                request.denominacion(),
                request.plantillaMemo(),
                request.plantillaRes()
        );

        EscuelaProfesional creado = gestionarEscuelaUseCase.modificarEscuela(command);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(creado), "Escuela Profesional actualizada!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> obtenerPorId(@PathVariable Long id) {
        EscuelaProfesionalProjection projection = gestionarEscuelaUseCase.obtenerDetallePorId(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.fromProjectionToResponse(projection), "Escuela Profesional encontrada!", "200");
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/listarPorFacultad/{facultadId}")
    public ResponseEntity<ResponseDTO<Object>> listarPorFacultad(@PathVariable Long facultadId) {
        List<EscuelaProfesionalResponse> lista = gestionarEscuelaUseCase.listarTodosPorFacultad(facultadId).stream()
                .map(mapper::fromProjectionToResponse)
                .toList();

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(lista, "Lista de escuelas profesionales de una facultad", "200");

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/listarActivosPorFacultad/{facultadId}")
    public ResponseEntity<ResponseDTO<Object>> listarActivosPorFacultad(@PathVariable Long facultadId) {
        List<EscuelaProfesionalResponse> lista = gestionarEscuelaUseCase.listarActivosPorFacultad(facultadId).stream()
                .map(mapper::fromProjectionToResponse)
                .toList();

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(lista, "Lista de escuelas profesionales activas de una facultad", "200");

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/listarPorDepartamento/{departamentoId}")
    public ResponseEntity<ResponseDTO<Object>> listarPorDepartamento(@PathVariable Long departamentoId) {
        List<EscuelaProfesionalResponse> lista = gestionarEscuelaUseCase.listarPorDepartamento(departamentoId).stream()
                .map(mapper::toResponse)
                .toList();

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(lista, "Lista de escuelas profesionales de un Departamento Académico", "200");

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gestionarEscuelaUseCase.eliminarEscuela(id);
        return ResponseEntity.noContent().build();
    }

    private EscuelaProfesionalResponse toResponse(EscuelaProfesional domain) {
        return new EscuelaProfesionalResponse(
                domain.getId(),
                DepartamentoResponse.builder().id(domain.getDepartamentoAcademicoId()).build(),
                domain.getCodigo(),
                domain.getAbreviatura(),
                domain.getDenominacion(),
                domain.getPlantillaMemorando(),
                domain.getPlantillaResolucion(),
                domain.getEstado()
        );
    }
}
