package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Comision;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ComisionRepositoryPort {
    Comision guardar(Comision comision);
    Optional<ComisionDetalleView> buscarPorId(Long id);
    Optional<ComisionDetalleView> obtenerPorTesisYEtapa(UUID tesisId, String etapa);
    List<ComisionDetalleView> listarPorTesis(UUID tesisId);

    boolean existeComisionActivaPorTesisYEtapa(UUID tesisId, String etapaCodigo);
    boolean existeComisionActiva(Long comisionId);
}
