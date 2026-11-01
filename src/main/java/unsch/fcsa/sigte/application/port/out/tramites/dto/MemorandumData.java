package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.time.LocalDate;
import java.util.List;

public record MemorandumData(
        String numeroMemo,
        String anio,
        List<MiembroMemo> miembros,
        String asunto,
        String tesistas,
        String numeroExpediente,
        LocalDate fechaEmision,
        String tipoDocumento,
        String tituloTesis,
        String rutaPlantilla
) {

    public record MiembroMemo(String nombre, String rol) {}
}
