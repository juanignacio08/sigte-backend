package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record RegistrarPlantillaDocumentoRequest(
        @NotNull(message = "La escuela es obligatoria")
        Long escuelaId,

        @NotBlank(message = "El tipo de documento es obligatorio")
        @Size(min = 6, max = 6, message = "El código debe tener 6 caracteres")
        String tipoDocumentoCodigo,

        @Min(value = 1, message = "El plazo en días debe ser mayor a cero")
        Integer plazoDias,

        @Min(value = 1, message = "El plazo en meses debe ser mayor a cero")
        Integer plazoMeses,

        @NotNull(message = "El archivo .docx es obligatorio")
        MultipartFile archivoDocx
) {}