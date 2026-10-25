package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.tramites;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarComisionUseCase;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionDetalleView;
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

    @PostMapping("/registrarMiembro")
    public ResponseEntity<ResponseDTO<Object>> registrarMiembro(@Valid @RequestBody ComisionMiembroRequest request) {
        ComisionMiembro miembro = useCase.agregarMienbro(mapper.toAgregar(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGlobalMapper.build(
                mapper.toMiembroResponse(miembro), "Miembro agregado correctamente", "201"));
    }

    @PostMapping("/miembro/actualizarRol/{id}/{rol}")
    public ResponseEntity<ResponseDTO<Object>> actualizarRolMiembro(@PathVariable Long id, @PathVariable String rol) {
        ComisionMiembro miembro = useCase.actualizarRol(id, rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseGlobalMapper.build(
                mapper.toMiembroResponse(miembro), "Actualizado el rol del miembro correctamente", "201"));
    }

    @DeleteMapping("/miembro/{id}")
    public ResponseEntity<ResponseDTO<Object>> eliminarMiembro(@PathVariable Long id) {
        useCase.quitarMiembro(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(null, "Miembro eliminado!", "200");
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/{tesisId}/{etapa}")
    public ResponseEntity<ResponseDTO<Object>> obtenerPorTesisYEtapae(@PathVariable UUID tesisId, @PathVariable String etapa) {
        ComisionDetalleView view = useCase.obtenerPorTesisYEtapa(tesisId, etapa);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.fromViewToResponse(view), "Comision de una tesis en una etapa", "200");
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> obtenerDetalle(@PathVariable Long id) {
        ComisionDetalleView view = useCase.obtenerComisionDetalle(id);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.fromViewToResponse(view), "Comisiones a detalle", "200");
        return ResponseEntity.ok(responseDTO);
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