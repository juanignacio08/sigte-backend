package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.maestro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.command.maestro.DepartamentoCommand;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.DepartamentoRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.DepartamentoResponse;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarDepartamentoUseCase;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro.DepartamentoMapperExpose;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final GestionarDepartamentoUseCase departamentoUseCase;

    private final DepartamentoMapperExpose mapper;

    public DepartamentoController(GestionarDepartamentoUseCase departamentoUseCase, DepartamentoMapperExpose mapper) {
        this.departamentoUseCase = departamentoUseCase;
        this.mapper = mapper;
    }

    @PostMapping("/guardar")
    public ResponseEntity<ResponseDTO<Object>> crear(@Valid @RequestBody DepartamentoRequest request) {
        DepartamentoCommand command = DepartamentoCommand.create(
                request.facultadId(), request.abreviatura(), request.denominacion()
        );

        DepartamentoAcademico creado = departamentoUseCase.crearDepartamento(command);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(creado), "Departamento Académico registrado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/listar")
    public ResponseEntity<ResponseDTO<Object>> listar() {
        List<DepartamentoResponse> lista = departamentoUseCase.listarTodos().stream()
                .map(this::toResponse)
                .toList();

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(lista, "", "200");

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartamentoResponse> obtenerPorId(@PathVariable Long id) {
        DepartamentoAcademico dominio = departamentoUseCase.obtenerPorId(id);
        return ResponseEntity.ok(toResponse(dominio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        departamentoUseCase.eliminarDepartamento(id);
        return ResponseEntity.noContent().build();
    }

    // Mapper helper: Convertir Modelo de Dominio -> DTO de Respuesta REST
    private DepartamentoResponse toResponse(DepartamentoAcademico domain) {
        return new DepartamentoResponse(
                domain.getId(),
                domain.getFacultadId(),
                domain.getAbreviatura(),
                domain.getDenominacion(),
                domain.getEstado()
        );
    }
}
