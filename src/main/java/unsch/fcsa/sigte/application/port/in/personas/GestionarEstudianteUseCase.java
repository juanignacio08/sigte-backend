package unsch.fcsa.sigte.application.port.in.personas;

import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.personas.EstudianteCommand;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteDetalleView;
import unsch.fcsa.sigte.application.command.personas.EstudianteFiltro;
import unsch.fcsa.sigte.domain.model.personas.Estudiante;

public interface GestionarEstudianteUseCase {
    Estudiante guardar(EstudianteCommand command);
    Estudiante actualizar(EstudianteCommand command);
    Estudiante obtenerPorId(Long id);
    EstudianteDetalleView obtenerDetallePorId(Long id);
    PageResult<EstudianteDetalleView> listarPorFiltros(EstudianteFiltro filtro, int page, int size);
    Estudiante activar(Long id);
    Estudiante desactivar(Long id);
    void eliminar(Long id);
}