package unsch.fcsa.sigte.application.port.out.storage;

import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoGenerado;
import unsch.fcsa.sigte.application.port.out.tramites.dto.MemorandumData;

import java.util.List;

public interface DocumentoGeneradorPort {
    DocumentoGenerado generarMemorandum(MemorandumData data);
    void eliminarArchivosGenerados(List<String> rutas);
}
