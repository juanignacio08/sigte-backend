package unsch.fcsa.sigte.application.port.in.personas;

import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.personas.DocenteCommand;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteDetalleView;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteFiltro;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteMetricasProjection;
import unsch.fcsa.sigte.domain.model.personas.Docente;

public interface GestionarDocenteUseCase {
    Docente guardar(DocenteCommand command);
    Docente actualizar(DocenteCommand command);
    PageResult<DocenteDetalleView> listarPorFiltros(DocenteFiltro filtro, int page, int size);
    DocenteMetricasProjection obtenerMetricasPorFacultad(Long facultadId);
    Docente activar(Long id);
    Docente desactivar(Long id);
    void eliminar(Long id);
    Docente obtenerPorNumDocumento(String tipoDocumento, String numDocumento);
    Docente obtenerPorId(Long id);
}
