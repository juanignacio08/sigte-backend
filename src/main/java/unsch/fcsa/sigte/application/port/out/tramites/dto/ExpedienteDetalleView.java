package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ExpedienteDetalleView(
        Long id,
        UUID tesisId,
        String numeroExpediente,
        String etapaCodigo,
        Integer numeroRevision,
        OffsetDateTime fechaIngreso,
        Integer estado,
        List<ExpedienteArchivoDetalleView> archivos
) {}
