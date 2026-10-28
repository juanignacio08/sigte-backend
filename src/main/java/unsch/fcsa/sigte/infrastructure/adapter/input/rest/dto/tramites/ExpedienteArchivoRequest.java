package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ExpedienteArchivoRequest (
        @NotNull(message = "El identificador del expediente es requerido")
        Long expedienteId,

        @NotNull(message = "Debe adjuntar al menos un archivo PDF")
        MultipartFile archivo
) {
}
