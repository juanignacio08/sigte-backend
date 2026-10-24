package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.tramites;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarComisionUseCase;
import unsch.fcsa.sigte.domain.model.tramites.Comision;
import unsch.fcsa.sigte.domain.model.tramites.ComisionMiembro;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.*;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.tramites.ComisionRestMapper;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comision")
public class ComisionController {

    private final GestionarComisionUseCase useCase;
    private final ComisionRestMapper mapper;

    public ComisionController(GestionarComisionUseCase useCase, ComisionRestMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Object>> registrar(@Valid @RequestBody ComisionRequest request) {
        Comision comision = useCase.registrar(mapper.toCreate(request));
        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                mapper.toResponse(comision), "Comisión registrada correctamente", "201");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> actualizar(@PathVariable Long id, @Valid @RequestBody ComisionRequest request) {
        Comision comision = useCase.actualizar(mapper.toUpdate(id, request));
        ResponseDTO<Object> body = ResponseGlobalMapper.build(
                mapper.toResponse(comision), "Comisión actualizada correctamente", "201");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/registrarMiembro")
    public ResponseEntity<ResponseDTO<Object>> registrarMiembro(@Valid @RequestBody ComisionMiembroRequest request) {
        ComisionMiembro miembro = useCase.agregarMienbro(mapper.toAgregar(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGlobalMapper.build(
                mapper.toMiembroResponse(miembro), "Miembro agregado correctamente", "201"));
    }

    @GetMapping("/listarPorTesis/{id}")
    public ResponseEntity<ResponseDTO<Object>> obtenerListaPorTesis(@PathVariable UUID id) {
        List<ComisionResponse> list = useCase.listarPorTesis(id).stream()
                .map(mapper::fromViewToResponse)
                .toList();

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(list, "Lista de comisiones de una tesis", "200");
        return ResponseEntity.ok(responseDTO);
    }

}