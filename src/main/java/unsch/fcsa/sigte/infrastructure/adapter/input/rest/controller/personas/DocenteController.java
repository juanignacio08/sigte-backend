package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.personas;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.port.in.personas.GestionarDocenteUseCase;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.domain.model.personas.Docente;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.DocenteRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.personas.DocenteRestMapper;

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
