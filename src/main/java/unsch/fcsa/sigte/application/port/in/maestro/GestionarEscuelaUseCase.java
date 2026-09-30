package unsch.fcsa.sigte.application.port.in.maestro;

import org.springframework.web.multipart.MultipartFile;
import unsch.fcsa.sigte.application.command.maestro.EscuelaCommand;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.EscuelaProfesionalProjection;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;

import java.util.List;

public interface GestionarEscuelaUseCase {
    EscuelaProfesional crearEscuela(EscuelaCommand command, MultipartFile plantillaMemo, MultipartFile plantillaRes);
    EscuelaProfesional modificarEscuela(EscuelaCommand command, MultipartFile plantillaMemo, MultipartFile plantillaRes);
    EscuelaProfesional obtenerPorId(Long id);
    EscuelaProfesionalProjection obtenerDetallePorId(Long id);
    List<EscuelaProfesionalProjection> listarTodosPorFacultad(Long facultadId);
    List<EscuelaProfesionalProjection> listarActivosPorFacultad(Long facultadId);
    List<EscuelaProfesional> listarPorDepartamento(Long departamentoId);
    EscuelaProfesional activarEscuela(Long id);
    EscuelaProfesional desactivarEscuela(Long id);
    void eliminarEscuela(Long id);
}
