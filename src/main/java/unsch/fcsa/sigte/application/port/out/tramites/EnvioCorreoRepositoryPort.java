package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.domain.model.tramites.EnvioCorreo;

import java.util.List;
import java.util.Optional;

public interface EnvioCorreoRepositoryPort {
    EnvioCorreo guardar(EnvioCorreo envio);
    Optional<EnvioCorreo> buscarPorId(Long id);
    List<EnvioCorreo> listarPorExpediente(Long expedienteId);
    boolean existeEnvioExitosoPorExpediente(Long expedienteId);
}