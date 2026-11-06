package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.tramites.TesisFiltro;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;

import java.util.UUID;

public interface TesisRepositoryPort {
    Tesis guardar(Tesis tesis);
    TesisDetalleView obtenerDetallePorId(UUID id);
    PageResult<TesisDetalleView> listarPorFiltros(TesisFiltro filtro, int page, int size);
    boolean estudianteTieneTesisActiva(Long estudianteId);
    boolean existePorId(UUID id);

    void actualizarTituloDictaminado(UUID tesisId, String tituloDictaminado);
    //void actualizarAutores(UUID tesisId, List<Long> nuevosEstudiantesIds);
}
