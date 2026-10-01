package unsch.fcsa.sigte.application.port.out.personas;

import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteFiltro;
import unsch.fcsa.sigte.domain.model.personas.Docente;

import java.util.Optional;

public interface DocenteRepositoryPort {
    Docente guardar(Docente docente);
    Optional<Docente> obtenerPorId(Long id);
    //Optional<DocenteDetalleView> obtenerDetallePorId(Long id);
    boolean existePorCorreoInstitucional(String correo);
    boolean existePorDocumento(String tipoDocumentoCodigo, String numeroDocumento);

    //List<DocenteDetalleView> listarPaginado(DocenteFiltroQuery filtro, int page, int size);
    long contarPaginado(DocenteFiltro filtro);
}
