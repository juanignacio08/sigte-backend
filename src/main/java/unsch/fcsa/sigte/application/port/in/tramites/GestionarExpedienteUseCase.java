package unsch.fcsa.sigte.application.port.in.tramites;

import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.ActualizarExpedienteCommand;
import unsch.fcsa.sigte.application.command.tramites.RegistrarExpedienteCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GestionarExpedienteUseCase {
    ExpedienteTesis registrar(RegistrarExpedienteCommand command, List<ArchivoSubida> archivos, boolean validarTesisExistente);
    ExpedienteTesis actualizar(ActualizarExpedienteCommand command);
    void activar(Long id);
    void desactivar(Long id, String motivo);
    void eliminar(Long id);
    List<ExpedienteDetalleView> listarPorTesis(UUID tesisId);
    ExpedienteDetalleView obtenerPorId(Long id);
    ExpedienteDetalleView obtenerUltimoExpedienteTesisActivo(UUID tesisId);
    Map<UUID, ExpedienteDetalleView> obtenerUltimosExpedientes(List<UUID> tesisIds);
}
