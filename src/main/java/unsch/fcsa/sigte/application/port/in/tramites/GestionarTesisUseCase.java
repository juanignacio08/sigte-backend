package unsch.fcsa.sigte.application.port.in.tramites;

import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.TesisCommand;
import unsch.fcsa.sigte.application.command.tramites.TesisFiltro;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;

import java.util.List;
import java.util.UUID;

public interface GestionarTesisUseCase {
    Tesis registrarProyecto(TesisCommand command, List<ArchivoSubida> files);
    //Tesis editarProyecto(TesisCommand command);
    TesisDetalleView obtenerPorId(UUID id);
    //PageResult<TesisDetalleView> listarPorFiltros(TesisFiltro filtro, int page, int size);
}
