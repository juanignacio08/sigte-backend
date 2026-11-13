package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro;

import jakarta.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;

public record ActualizarPlantillaDocumentoRequest(
        @Min(value = 1, message = "El plazo en días debe ser mayor a cero")
        Integer plazoDias,

        @Min(value = 1, message = "El plazo en meses debe ser mayor a cero")
        Integer plazoMeses,

        MultipartFile archivoDocx
) {}
