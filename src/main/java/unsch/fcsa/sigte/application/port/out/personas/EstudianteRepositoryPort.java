package unsch.fcsa.sigte.application.port.out.personas;

import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteDetalleView;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteFiltro;
import unsch.fcsa.sigte.domain.model.personas.Estudiante;

import java.util.Optional;

public interface EstudianteRepositoryPort {
    Estudiante guardar(Estudiante estudiante);
    Optional<Estudiante> obtenerPorId(Long id);
    EstudianteDetalleView obtenerDetallePorId(Long id);
    boolean existePorCodigo(String codigo);
    boolean existePorNumeroDocumento(String numeroDocumento);
    boolean existePorCorreoInstitucional(String correoInstitucional);
    PageResult<EstudianteDetalleView> listarPorFiltros(EstudianteFiltro filtro, int page, int size);
}