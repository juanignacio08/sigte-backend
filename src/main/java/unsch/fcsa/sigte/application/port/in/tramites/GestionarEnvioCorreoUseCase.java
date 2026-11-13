package unsch.fcsa.sigte.application.port.in.tramites;

import unsch.fcsa.sigte.domain.model.tramites.EnvioCorreo;

import java.util.List;

public interface GestionarEnvioCorreoUseCase {
    List<EnvioCorreo> obtenerHistorialEnviosPorDocumento(Long documentoId);
}
