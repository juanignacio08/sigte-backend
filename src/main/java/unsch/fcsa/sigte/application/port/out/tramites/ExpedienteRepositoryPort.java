package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ExpedienteRepositoryPort {
    ExpedienteTesis guardar(ExpedienteTesis expediente);
    Optional<ExpedienteTesis> buscarPorId(Long id);
    Optional<ExpedienteDetalleView> obtenerDetallePorId(Long id);
    Optional<ExpedienteDetalleView> obtenerDetalleUltimoExpedienteDeTesis(UUID tesisId);
    List<ExpedienteDetalleView> listarPorTesisId(UUID tesisId);
    List<ExpedienteDetalleView> listarPorTesisIdYEtapa(UUID tesisId, String etapa);
    Map<UUID, ExpedienteDetalleView> buscarUltimosExpedientesPorTesisIds(List<UUID> tesisIds);

    boolean existeNumeroExpedienteActivo(String numeroExpediente);
    boolean existeExpedientePorEtapaYNumeroRevisionActivo(UUID id, String etapaCodigo, Integer numeroRevision);
    void eliminarExpediente(ExpedienteDetalleView expediente);
}
