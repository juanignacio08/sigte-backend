package unsch.fcsa.sigte.infrastructure.adapter.input.rest.maestro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.dto.maestro.CrearDepartamentoRequest;
import unsch.fcsa.sigte.application.dto.maestro.DepartamentoResponse;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.domain.port.in.maestro.GestionarDepartamentoUseCase;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final GestionarDepartamentoUseCase departamentoUseCase;

    // Inyectamos el Puerto de Entrada (La interfaz, no la implementación directamente)
    public DepartamentoController(GestionarDepartamentoUseCase departamentoUseCase) {
        this.departamentoUseCase = departamentoUseCase;
    }

    @PostMapping("/guardar")
    public ResponseEntity<DepartamentoResponse> crear(@Valid @RequestBody CrearDepartamentoRequest request) {
        DepartamentoAcademico creado = departamentoUseCase.crearDepartamento(
                request.facultadId(),
                request.abreviatura(),
                request.denominacion()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(creado));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<DepartamentoResponse>> listar() {
        List<DepartamentoResponse> lista = departamentoUseCase.listarTodos().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
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
