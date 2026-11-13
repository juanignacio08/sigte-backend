package unsch.fcsa.sigte.application.port.out.storage;

import org.springframework.core.io.Resource;
import unsch.fcsa.sigte.application.command.files.ArchivoSubida;

public interface StoragePort {
    String guardarArchivo(ArchivoSubida archivo, String subDirectorio);
    String guardarArchivoPdf(ArchivoSubida archivo, String subDirectorio);
    void eliminarArchivo(String rutaRelativa);
    void eliminarTrasCommit(String ruta);
    Resource cargarComoRecurso(String rutaRelativa);
}
