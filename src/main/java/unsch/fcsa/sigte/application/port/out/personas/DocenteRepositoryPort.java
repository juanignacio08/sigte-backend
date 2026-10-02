package unsch.fcsa.sigte.application.port.out.personas;

import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteDetalleView;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteFiltro;
import unsch.fcsa.sigte.domain.model.personas.Docente;

import java.util.Optional;

public interface DocenteRepositoryPort {
    Docente guardar(Docente docente);
    Optional<Docente> obtenerPorId(Long id);
    //Optional<DocenteDetalleView> obtenerDetallePorId(Long id);
    boolean existePorCorreoInstitucional(String correo);
    boolean existePorDocumento(String tipoDocumentoCodigo, String numeroDocumento);

    PageResult<DocenteDetalleView> listarPaginado(DocenteFiltro filtro, int page, int size);
}
