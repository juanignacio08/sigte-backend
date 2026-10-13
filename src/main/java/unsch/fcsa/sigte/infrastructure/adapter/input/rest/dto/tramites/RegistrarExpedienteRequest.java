package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.UUID;

public record RegistrarExpedienteRequest(
        @NotNull(message = "El identificador de la tesis es requerido")
        UUID tesisId,

        @NotBlank(message = "El número de expediente es obligatorio")
        @Size(max = 50, message = "El número de expediente no puede superar los 50 caracteres")
        String numeroExpediente,

        @NotBlank(message = "El código de etapa es obligatorio")
        @Size(min = 6, max = 6, message = "El código de etapa debe tener exactamente 6 caracteres")
        String etapaCodigo,

        @NotNull(message = "El número de revisión es obligatorio")
        Integer numeroRevision,

        @NotNull(message = "Debe adjuntar al menos un archivo PDF")
        @Size(min = 1, max = 5, message = "Puede adjuntar entre 1 y 5 archivos PDF")
        List<MultipartFile> archivos
) {}