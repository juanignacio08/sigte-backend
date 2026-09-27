package unsch.fcsa.sigte.application.port.in.maestro;

import unsch.fcsa.sigte.application.command.maestro.EscuelaCommand;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;

import java.util.List;

public interface GestionarEscuelaUseCase {
    EscuelaProfesional crearEscuela(EscuelaCommand command);
    EscuelaProfesional modificarEscuela(EscuelaCommand command);
    EscuelaProfesional obtenerPorId(Long id);
    List<EscuelaProfesional> listarTodosPorFacultad(Long facultadId);
    List<EscuelaProfesional> listarActivosPorFacultad(Long facultadId);
    List<EscuelaProfesional> listarTodosPorDepartamento(Long departamentoId);
    EscuelaProfesional activarEscuela(Long id);
    EscuelaProfesional desactivarEscuela(Long id);
    void eliminarEscuela(Long id);
}
