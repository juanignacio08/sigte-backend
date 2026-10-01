package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.maestro;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarMaestrosUseCase;
import unsch.fcsa.sigte.domain.model.maestro.Maestro;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.MaestroRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.MaestroResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro.MasterMapperExpose;

import java.util.List;

@RestController
@RequestMapping("/maestros")
@RequiredArgsConstructor
public class MaestroController {

    private final GestionarMaestrosUseCase gestionarMaestrosUseCase;
    private final MasterMapperExpose mapper;

    @PostMapping
    public ResponseEntity<ResponseDTO<Object>> crear(@Valid @RequestBody MaestroRequest request) {
        Maestro creado = gestionarMaestrosUseCase.create(
                request.tableCode(),
                request.itemCode(),
                request.denomination(),
                request.isSystem()
        );

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(creado), "Registro guardado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{code}")
    public ResponseEntity<ResponseDTO<Object>> editar(@PathVariable String code, @Valid @RequestBody MaestroRequest request) {
        Maestro actualizado = gestionarMaestrosUseCase.update(
                code,
                request.denomination(),
                request.isSystem()
        );

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(actualizado), "Registro actualizado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/getTables")
    public ResponseEntity<ResponseDTO<Object>> getTables(@RequestParam Boolean isSystem) {
        List<MaestroResponse> lista = gestionarMaestrosUseCase.getTables(isSystem).stream()
                .map(mapper::toResponse)
                .toList();

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(lista, "Lista de tablas", "200");

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/getItems/{tableCode}")
    public ResponseEntity<ResponseDTO<Object>> getItems(@PathVariable String tableCode) {
        List<MaestroResponse> lista = gestionarMaestrosUseCase.getItemsByTableCode(tableCode).stream()
                .map(mapper::toResponse)
                .toList();

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(lista, "Lista de items", "200");

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/getItemsActives/{tableCode}")
    public ResponseEntity<ResponseDTO<Object>> getItemsActives(@PathVariable String tableCode) {
        List<MaestroResponse> lista = gestionarMaestrosUseCase.getItemsActivesByTableCode(tableCode).stream()
                .map(mapper::toResponse)
                .toList();

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(lista, "Lista de items", "200");

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("activate/{code}")
    public ResponseEntity<ResponseDTO<Object>> activate(@PathVariable String code) {
        Maestro dominio = gestionarMaestrosUseCase.activate(code);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(dominio), "Registro activado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("deactivate/{code}")
    public ResponseEntity<ResponseDTO<Object>> deactivate(@PathVariable String code) {
        Maestro dominio = gestionarMaestrosUseCase.deactivate(code);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(dominio), "Registro desactivado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("{code}")
    public ResponseEntity<ResponseDTO<Object>> delete(@PathVariable String code) {
        gestionarMaestrosUseCase.delete(code);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(null, "Registro eliminado!", "200");
        return ResponseEntity.ok().body(responseDTO);
    }

}
