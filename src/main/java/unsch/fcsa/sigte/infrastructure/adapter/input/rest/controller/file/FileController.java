package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.file;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/file")
public class FileController {

    private final StoragePort storagePort;

    public FileController(StoragePort storagePort) {
        this.storagePort = storagePort;
    }

    @GetMapping("/plantillas/descargar")
    public ResponseEntity<Resource> descargarPlantilla(@RequestParam("ruta") String ruta) {
        Resource recurso = storagePort.cargarComoRecurso(ruta);

        // MimeType para documentos Word (.docx)
        String contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

        // Extraer el nombre original del archivo desde la ruta
        String filename = recurso.getFilename();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(recurso);
    }

    @GetMapping("descargarPdf")
    public ResponseEntity<Resource> descargarPdf(@RequestParam("ruta") String ruta) {
        Resource recurso = storagePort.cargarComoRecurso(ruta);
        String filename = recurso.getFilename();

        // Escapamos comillas y codificamos UTF-8 por si el nombre tiene tildes/ñ
        String safeName = filename == null ? "documento.pdf" : filename.replace("\"", "");
        String encodedName = URLEncoder.encode(safeName, StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + safeName + "\"; filename*=UTF-8''" + encodedName)
                .body(recurso);
    }

}
