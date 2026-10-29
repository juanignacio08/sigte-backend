package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteArchivoDetalleView;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExpedienteResponse(
        Long id,
        UUID tesisId,
        String numeroExpediente,
        String etapaCodigo,
        String etapaDenominacion,
        Integer numRevision,
        String estadoCodigo,
        String estadoDenominacion,
        Integer ultimoPaso,
        Long comisionId,
        OffsetDateTime fechaAsociacion,
        Integer estado,
        List<ExpedienteArchivoDetalleView> archivos
) {}
