package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.application.command.PageResult;
import unsch.fcsa.sigte.application.command.tramites.TesisFiltro;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TesisRepositoryPort {
    Tesis guardar(Tesis tesis);
    //Optional<Tesis> obtenerPorId(UUID id);
    TesisDetalleView obtenerDetallePorId(UUID id);
    //PageResult<TesisDetalleView> listarPorFiltros(TesisFiltro filtro, int page, int size);
    boolean existeExpedientePorNumero(String numeroExpediente);
    boolean estudianteTieneTesisActiva(Long estudianteId);
    //boolean estudianteTieneTesisActivaExcluyendo(Long estudianteId, UUID tesisIdActual);
    //void actualizarAutores(UUID tesisId, List<Long> nuevosEstudiantesIds);
}
