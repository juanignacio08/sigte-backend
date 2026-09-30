package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import unsch.fcsa.sigte.application.port.out.storage.StoragePort;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@Slf4j
public class LocalStorageAdapter implements StoragePort {

    private final Path rootPath;

    public LocalStorageAdapter(@Value("${app.storage.location:./storage}") String storagePath) {
        this.rootPath = Paths.get(storagePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootPath);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio raíz de almacenamiento", e);
        }
    }

    @Override
    public String guardarArchivo(MultipartFile archivo, String subDirectorio) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        log.info("Guardando archivo {} ", archivo.getOriginalFilename());
        // Validación de tipo de archivo (solo Word)
        String contentType = archivo.getContentType();
        String originalFilename = archivo.getOriginalFilename();
        if (originalFilename == null || (!originalFilename.endsWith(".doc") && !originalFilename.endsWith(".docx"))) {
            throw new IllegalArgumentException("Solo se permiten archivos en formato Word (.doc, .docx)");
        }

        try {
            Path targetDir = this.rootPath.resolve(subDirectorio).normalize();
            Files.createDirectories(targetDir);

            // Generar nombre único para evitar colisiones
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID() + extension;

            Path destinationFile = targetDir.resolve(filename).normalize();

            // Evitar Path Traversal
            if (!destinationFile.startsWith(this.rootPath)) {
                throw new SecurityException("No se permite guardar archivos fuera del directorio designado");
            }

            try (InputStream inputStream = archivo.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Retorna ruta relativa normalizada (ej: "plantillas/uuid.docx")
            log.info("Guardando archivo {} ", filename);
            return this.rootPath.relativize(destinationFile).toString().replace('\\', '/');

        } catch (IOException e) {
            throw new RuntimeException("Error al almacenar el archivo", e);
        }
    }

    @Override
    public void eliminarArchivo(String rutaRelativa) {
        if (rutaRelativa == null || rutaRelativa.isBlank()) return;

        log.info("Eliminando archivo {} ", rutaRelativa);
        try {
            Path fileToDelete = this.rootPath.resolve(rutaRelativa).normalize();
            if (fileToDelete.startsWith(this.rootPath) && Files.exists(fileToDelete)) {
                Files.delete(fileToDelete);
            }
        } catch (IOException e) {
            // Log de advertencia si no se pudo eliminar
            log.error("Error al eliminar el archivo. No se pudo eliminar", e);
        }
    }

    @Override
    public Resource cargarComoRecurso(String rutaRelativa) {
        log.info("Cargando el archivo {} ", rutaRelativa);
        try {
            Path filePath = this.rootPath.resolve(rutaRelativa).normalize();

            // Seguridad: Evitar Path Traversal
            if (!filePath.startsWith(this.rootPath)) {
                throw new SecurityException("Acceso no autorizado al recurso");
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("Documento", "Ruta", rutaRelativa);
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Documento", "Ruta", rutaRelativa);
        }
    }
}
