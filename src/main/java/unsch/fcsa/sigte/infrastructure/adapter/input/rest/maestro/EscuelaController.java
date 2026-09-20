package unsch.fcsa.sigte.infrastructure.adapter.input.rest.maestro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalResponse;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarEscuelaUseCase;

import java.util.List;

@RestController
@RequestMapping("/escuelas")
public class EscuelaController {

    private final GestionarEscuelaUseCase gestionarEscuelaUseCase;

    public EscuelaController(GestionarEscuelaUseCase gestionarEscuelaUseCase) {
        this.gestionarEscuelaUseCase = gestionarEscuelaUseCase;
    }

    @PostMapping("guardar")
    public ResponseEntity<EscuelaProfesionalResponse> crear(@RequestBody @Valid EscuelaProfesionalRequest request) {
        EscuelaProfesional creado = gestionarEscuelaUseCase.crearEscuela(
                request.departamentoId(),
                request.codigo(),
                request.abreviatura(),
                request.denominacion(),
                request.plantillaMemo(),
                request.plantillaRes()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creado));
    }

    @PutMapping("modificar")
    public ResponseEntity<EscuelaProfesionalResponse> modificar(@RequestBody @Valid EscuelaProfesionalRequest request) {
        EscuelaProfesional creado = gestionarEscuelaUseCase.modificarEscuela(
                request.id(),
                request.departamentoId(),
                request.codigo(),
                request.abreviatura(),
                request.denominacion(),
                request.plantillaMemo(),
                request.plantillaRes()
        );

        return ResponseEntity.status(200).body(toResponse(creado));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<EscuelaProfesionalResponse>> listar() {
        List<EscuelaProfesionalResponse> lista = gestionarEscuelaUseCase.listarTodos().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gestionarEscuelaUseCase.eliminarEscuela(id);
        return ResponseEntity.noContent().build();
    }

    private EscuelaProfesionalResponse toResponse(EscuelaProfesional domain) {
        return new EscuelaProfesionalResponse(
                domain.getId(),
                domain.getDepartamentoAcademicoId(),
                domain.getCodigo(),
                domain.getAbreviatura(),
                domain.getDenominacion(),
                domain.getPlantillaMemorando(),
                domain.getPlantillaResolucion(),
                domain.getEstado()
        );
    }
}
