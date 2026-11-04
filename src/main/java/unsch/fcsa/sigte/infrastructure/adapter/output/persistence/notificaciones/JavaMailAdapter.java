package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.notificaciones;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.notificaciones.EmailPort;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Component
public class JavaMailAdapter implements EmailPort {

    private final JavaMailSender mailSender;
    private final Path storageRoot;
    private final String remitente;

    public JavaMailAdapter(
            JavaMailSender mailSender,
            @Value("${app.storage.location}") String storageLocation,
            @Value("${spring.mail.username}") String remitente) {
        this.mailSender = mailSender;
        this.storageRoot = Paths.get(storageLocation).toAbsolutePath().normalize();
        this.remitente = remitente;
    }

    @Override
    public String enviarConAdjuntos(
            List<String> destinatarios,
            String asunto,
            String cuerpo,
            List<String> rutasAdjuntos
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // true = multipart (necesario para adjuntos)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(remitente);
            helper.setTo(destinatarios.toArray(new String[0]));
            helper.setSubject(asunto);
            helper.setText(cuerpo, false); // false = texto plano, true = HTML

            adjuntarArchivos(helper, rutasAdjuntos);

            mailSender.send(message);
            log.info("Correo enviado. Destinatarios: {} · Adjuntos: {}",
                    destinatarios.size(), rutasAdjuntos != null ? rutasAdjuntos.size() : 0);

            return null; // Éxito

        } catch (Exception e) {
            log.error("Error al enviar correo. Asunto: '{}' · Destinatarios: {}",
                    asunto, destinatarios, e);
            return e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        }
    }

    private void adjuntarArchivos(MimeMessageHelper helper, List<String> rutasAdjuntos) throws Exception {
        if (rutasAdjuntos == null || rutasAdjuntos.isEmpty()) return;

        for (String rutaRelativa : rutasAdjuntos) {
            if (rutaRelativa == null || rutaRelativa.isBlank()) continue;

            Path rutaAbsoluta = storageRoot.resolve(rutaRelativa).normalize();

            if (!rutaAbsoluta.startsWith(storageRoot)) {
                log.warn("Ruta de adjunto fuera del storage, ignorando: {}", rutaRelativa);
                continue;
            }

            File archivo = rutaAbsoluta.toFile();
            if (!archivo.exists()) {
                log.warn("Adjunto no encontrado en disco, ignorando: {}", rutaAbsoluta);
                continue;
            }

            FileSystemResource resource = new FileSystemResource(archivo);
            helper.addAttachment(resource.getFilename(), resource);
        }
    }
}
