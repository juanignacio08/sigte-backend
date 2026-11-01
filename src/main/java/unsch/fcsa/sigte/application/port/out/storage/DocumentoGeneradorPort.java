package unsch.fcsa.sigte.application.port.out.storage;

import unsch.fcsa.sigte.application.port.out.tramites.dto.DocumentoGenerado;
import unsch.fcsa.sigte.application.port.out.tramites.dto.MemorandumData;

public interface DocumentoGeneradorPort {
    DocumentoGenerado generarMemorandum(MemorandumData data);
}
