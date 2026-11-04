package unsch.fcsa.sigte.infrastructure.adapter.input.rest.controller.tramites;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import unsch.fcsa.sigte.application.port.in.tramites.GestionarEnvioCorreoUseCase;
import unsch.fcsa.sigte.application.port.out.tramites.dto.DestinatarioEmail;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteArchivoDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.EnvioCorreo;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.base.ResponseDTO;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.EnvioCorreoResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.ResponseGlobalMapper;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/envio_correo")
public class EnvioCorreoController {

    private final GestionarEnvioCorreoUseCase gestionarEnvioCorreoUseCase;

    public EnvioCorreoController(GestionarEnvioCorreoUseCase useCase) {
        this.gestionarEnvioCorreoUseCase = useCase;
    }

    @GetMapping("/historial/{expedienteId}")
    public ResponseEntity<ResponseDTO<Object>> historial(@PathVariable Long expedienteId) {
        List<EnvioCorreo> list = gestionarEnvioCorreoUseCase.obtenerHistorialEnviosPorExpediente(expedienteId);

        ResponseDTO<Object> responseDTO = ResponseGlobalMapper.build(
                list.stream().map(this::toResponse).toList(), "HIstorial de envios.", "200"
        );

        return ResponseEntity.ok(responseDTO);
    }

    private EnvioCorreoResponse toResponse(EnvioCorreo domain) {
        if (domain == null) {
            return null;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<DestinatarioEmail> destinatarios =
                    domain.getDestinatariosJson() == null || domain.getDestinatariosJson().isBlank()
                            ? Collections.emptyList()
                            : objectMapper.readValue(
                            domain.getDestinatariosJson(),
                            new TypeReference<>() {
                            }
                    );

            List<String> urls =
                    domain.getAdjuntosJson() == null || domain.getAdjuntosJson().isBlank()
                            ? Collections.emptyList()
                            : objectMapper.readValue(
                            domain.getAdjuntosJson(),
                            new TypeReference<>() {
                            }
                    );

            List<ExpedienteArchivoDetalleView> adjuntos = urls.stream()
                    .map(ruta -> new ExpedienteArchivoDetalleView(
                            1L,
                            obtenerNombreArchivo(ruta),
                            ruta,
                            1000L
                    ))
                    .toList();

            return EnvioCorreoResponse.builder()
                    .id(domain.getId())
                    .expedienteId(domain.getExpedienteId())
                    .asunto(domain.getAsunto())
                    .destinatarios(destinatarios)
                    .adjuntos(adjuntos)
                    .fechaEnvio(domain.getFechaEnvio())
                    .estado("Enviado con éxito")
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error al deserializar los datos del envío de correo", e);
        }
    }

    private String obtenerNombreArchivo(String rutaRelativa) {
        if (rutaRelativa == null || rutaRelativa.isBlank()) {
            return null;
        }

        return Paths.get(rutaRelativa).getFileName().toString();
    }
}
