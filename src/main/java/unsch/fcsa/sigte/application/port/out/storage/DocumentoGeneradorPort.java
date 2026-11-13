package unsch.fcsa.sigte.application.port.out.storage;

import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoGenerado;
import unsch.fcsa.sigte.application.port.out.tramites.dto.MemorandumData;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ResolutionProjectData;

import java.util.List;

public interface DocumentoGeneradorPort {
    DocumentoGenerado generarMemorandum(MemorandumData data);
    DocumentoGenerado generarResolucionProjectAprobacion(ResolutionProjectData data);
    void eliminarArchivosGenerados(List<String> rutas);
}
