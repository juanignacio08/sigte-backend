package unsch.fcsa.sigte.application.port.in.tramites;

import unsch.fcsa.sigte.application.command.files.ArchivoSubida;
import unsch.fcsa.sigte.application.command.tramites.ActualizarExpedienteCommand;
import unsch.fcsa.sigte.application.command.tramites.RegistrarExpedienteCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;

import java.util.List;
import java.util.UUID;

public interface GestionarExpedienteUseCase {
    ExpedienteTesis registrar(RegistrarExpedienteCommand command, List<ArchivoSubida> archivos);
    //ExpedienteDetalleView actualizar(ActualizarExpedienteCommand command);
    //void darDeBaja(Long id, String motivo);
    List<ExpedienteDetalleView> listarPorTesis(UUID tesisId);
    ExpedienteDetalleView obtenerPorId(Long id);
}
