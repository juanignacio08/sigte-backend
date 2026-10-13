package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpedienteRepositoryPort {
    ExpedienteTesis guardar(ExpedienteTesis expediente);
    //Optional<ExpedienteTesis> buscarPorId(Long id);
    //Optional<ExpedienteDetalleView> obtenerDetallePorId(Long id);
    List<ExpedienteDetalleView> listarPorTesisId(UUID tesisId);
    boolean existeNumeroExpedienteActivo(String numeroExpediente);
    boolean existeExpedientePorEtapaYNumeroRevisionActivo(String etapaCodigo, Integer numeroRevision);
}
