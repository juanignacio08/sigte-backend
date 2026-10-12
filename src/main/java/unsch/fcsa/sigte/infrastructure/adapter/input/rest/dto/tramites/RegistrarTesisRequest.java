package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record RegistrarTesisRequest(
        @NotNull(message = "El ID de la escuela es obligatorio")
        Long escuelaId,

        @NotBlank(message = "El título de la tesis es obligatorio")
        String titulo,

        @NotEmpty(message = "Debe proporcionar al menos un estudiante como autor")
        @Size(min = 1, max = 2, message = "La tesis debe tener entre 1 y 2 autores")
        List<Long> estudiantesIds,

        @NotBlank(message = "El número de expediente es obligatorio")
        @Size(max = 50, message = "El número de expediente no debe superar 50 caracteres")
        String numeroExpediente,

        @Size(min = 6, max = 6, message = "El código de la etapa de tesis debe tener exactamente 6 caracteres")
        String etapaTesisCodigo,

        Integer numeroRevision,

        @NotEmpty(message = "Debe proporcionar al menos un archivo pdf")
        @Size(min = 1, max = 5, message = "Se aceptan entre 1 a 5 archivos pdfs")
        List<MultipartFile> archivosPdfs
) {}