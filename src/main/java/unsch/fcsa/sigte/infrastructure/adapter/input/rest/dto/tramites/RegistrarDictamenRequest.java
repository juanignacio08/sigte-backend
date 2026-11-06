package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record RegistrarDictamenRequest(
        @NotNull(message = "El ID del expediente es obligatorio")
        Long expedienteId,

        @NotBlank(message = "El resultado del dictamen es obligatorio")
        @Pattern(regexp = "008001|008002",
                message = "El resultado debe ser APROBADO u OBSERVADO")
        String resultadoCodigo,

        String observaciones,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate fechaEmision,

        MultipartFile archivoPdf,

        MultipartFile archivoWord
) {}