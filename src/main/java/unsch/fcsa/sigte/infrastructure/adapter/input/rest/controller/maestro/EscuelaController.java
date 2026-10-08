package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.maestro;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.maestro.EscuelaCommand;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.EscuelaProfesionalProjection;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalResponse;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarEscuelaUseCase;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.file.MultipartFileMapper;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro.EscuelaProfesionalMapperExpose;

import java.util.List;

@RestController
@RequestMapping("/escuelas")
public class EscuelaController {

    private final GestionarEscuelaUseCase gestionarEscuelaUseCase;
    private final StoragePort storagePort;

    private final EscuelaProfesionalMapperExpose mapper;
    private final MultipartFileMapper multipartFileMapper;

    public EscuelaController(GestionarEscuelaUseCase gestionarEscuelaUseCase, EscuelaProfesionalMapperExpose mapper, StoragePort storagePort, MultipartFileMapper multipartFileMapper) {
        this.gestionarEscuelaUseCase = gestionarEscuelaUseCase;
        this.mapper = mapper;
        this.storagePort = storagePort;
        this.multipartFileMapper = multipartFileMapper;
    }

    @PostMapping(
            value = "/guardar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDTO<Object>> crear(@ModelAttribute @Valid EscuelaProfesionalRequest request) {
        EscuelaCommand command = EscuelaCommand.create(
                request.departamentoId(),
                request.codigo(),
                request.abreviatura(),
                request.denominacion()
        );

        ArchivoSubida plantillaMemo = multipartFileMapper.toArchivosSubida(request.plantillaMemo());
        ArchivoSubida plantillaRes = multipartFileMapper.toArchivosSubida(request.plantillaRes());

        EscuelaProfesional creado = gestionarEscuelaUseCase.crearEscuela(command, plantillaMemo, plantillaRes);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(creado), "Escuela Profesional registrado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping(
            value = "/modificar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResponseDTO<Object>> modificar(@ModelAttribute @Valid EscuelaProfesionalRequest request) {
        EscuelaCommand command = EscuelaCommand.update(
                request.id(),
                request.departamentoId(),
                request.codigo(),
                request.abreviatura(),
                request.denominacion()
        );

        ArchivoSubida plantillaMemo = multipartFileMapper.toArchivosSubida(request.plantillaMemo());
        ArchivoSubida plantillaRes = multipartFileMapper.toArchivosSubida(request.plantillaRes());

        EscuelaProfesional creado = gestionarEscuelaUseCase.modificarEscuela(command, plantillaMemo, plantillaRes);

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

    @PutMapping("activar/{id}")
    public ResponseEntity<ResponseDTO<Object>> activar(@PathVariable Long id) {
        EscuelaProfesional dominio = gestionarEscuelaUseCase.activarEscuela(id);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(dominio), "Escuela Profesional activado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("desactivar/{id}")
    public ResponseEntity<ResponseDTO<Object>> desactivar(@PathVariable Long id) {
        EscuelaProfesional dominio = gestionarEscuelaUseCase.desactivarEscuela(id);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(mapper.toResponse(dominio), "Escuela Profesional desactivado!", "201");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResponseDTO<Object>> eliminar(@PathVariable Long id) {
        gestionarEscuelaUseCase.eliminarEscuela(id);
        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(null, "Escuela Profesional eliminado!", "200");
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/plantillas/descargar")
    public ResponseEntity<Resource> descargarPlantilla(@RequestParam("ruta") String ruta) {
        Resource recurso = storagePort.cargarComoRecurso(ruta);

        // MimeType para documentos Word (.docx)
        String contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

        // Extraer el nombre original del archivo desde la ruta
        String filename = recurso.getFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(recurso);
    }

}
