package unsch.fcsa.sigte.application.port.out.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StoragePort {
    String guardarArchivo(MultipartFile archivo, String subDirectorio);
    void eliminarArchivo(String rutaRelativa);
    Resource cargarComoRecurso(String rutaRelativa);
}
