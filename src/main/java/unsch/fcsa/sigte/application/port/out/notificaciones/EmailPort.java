package unsch.fcsa.sigte.application.port.out.notificaciones;

import java.util.List;

public interface EmailPort {
    String enviarConAdjuntos(
            List<String> destinatarios,
            String asunto,
            String cuerpo,
            List<String> rutasAdjuntos
    );
}
