package unsch.fcsa.sigte.application.usecase.tramites;

import lombok.extern.slf4j.Slf4j;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarEnvioCorreoUseCase;
import unsch.fcsa.sigte.application.port.out.tramites.EnvioCorreoRepositoryPort;
import unsch.fcsa.sigte.domain.model.tramites.EnvioCorreo;

import java.util.List;

@Slf4j
public class GestionarEnvioCorreoUseCaseImpl implements GestionarEnvioCorreoUseCase {

    private final EnvioCorreoRepositoryPort envioCorreoRepositoryPort;

    public GestionarEnvioCorreoUseCaseImpl(
            EnvioCorreoRepositoryPort envioCorreoRepositoryPort
    ) {
        this.envioCorreoRepositoryPort = envioCorreoRepositoryPort;
    }

    @Override
    public List<EnvioCorreo> obtenerHistorialEnviosPorExpediente(Long expedienteId) {
        log.info("Obteniendo historial de envios de un expediente. [ EXPEDIENTE : {} ]", expedienteId.toString());
        return envioCorreoRepositoryPort.listarPorExpediente(expedienteId);
    }
}
