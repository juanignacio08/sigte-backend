package unsch.fcsa.sigte.application.port.in.personas;

import unsch.fcsa.sigte.application.command.personas.DocenteCommand;
import unsch.fcsa.sigte.domain.model.personas.Docente;

public interface GestionarDocenteUseCase {
    Docente guardar(DocenteCommand command);
    Docente actualizar(DocenteCommand command);
    void activar(Long id);
    void desactivar(Long id);
    void eliminar(Long id);
}
