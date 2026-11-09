package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DestinatarioEmail;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteArchivoDetalleView;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EnvioCorreoResponse (
    Long id,
    Long documentoId,
    String asunto,
    List<DestinatarioEmail> destinatarios,
    List<ExpedienteArchivoDetalleView> adjuntos,
    OffsetDateTime fechaEnvio,
    String estado
) { }
