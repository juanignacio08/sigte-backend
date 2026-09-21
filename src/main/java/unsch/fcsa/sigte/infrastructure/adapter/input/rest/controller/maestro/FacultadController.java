package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.maestro;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarFacultadUseCase;
import unsch.fcsa.sigte.domain.model.maestro.Facultad;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro.FacultadMapperExpose;

@RestController
@RequestMapping("/facultades")
public class FacultadController {

    private final GestionarFacultadUseCase gestionarFacultadUseCase;
    private final FacultadMapperExpose mapper;

    public FacultadController(GestionarFacultadUseCase gestionarFacultadUseCase,  FacultadMapperExpose mapper) {
        this.gestionarFacultadUseCase = gestionarFacultadUseCase;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Object>> obtenerPorId(@PathVariable Long id) {
        Facultad dominio = gestionarFacultadUseCase.buscarPorId(id);
        ResponseDTO<Object> response = ResponseGlobalMapper.build(mapper.toResponse(dominio), "Facultad encontrada!", "200");
        return ResponseEntity.ok(response);
    }

}
