package unsch.fcsa.sigte.domain.port.in.maestro;

import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;

import java.util.List;

public interface GestionarEscuelaUseCase {
    EscuelaProfesional crearEscuela(Long departamentoId, Integer codigo, String abreviatura, String denominacion, String plantillaMemo, String plantillaRes);
    EscuelaProfesional modificarEscuela(Long id, Long departamentoId, Integer codigo, String abreviatura, String denominacion,  String plantillaMemo, String plantillaRes);
    EscuelaProfesional obtenerPorId(Long id);
    List<EscuelaProfesional> listarTodos();
    void eliminarEscuela(Long id);
}
