package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocenteRequest(
        String codigo,

        @NotNull(message = "La escuela es requerida")
        Long escuelaId,

        @NotNull(message = "El departamento académico es requerido")
        Long departamentoAcademicoId,

        String tipoDocumentoCodigo,
        String numeroDocumento,

        @NotBlank(message = "El nombre es requerido")
        String nombres,

        @NotBlank(message = "El apellido paterno es requerido")
        String primerApellido,

        @NotBlank(message = "El apellido materno es requerido")
        String segundoApellido,

        @NotBlank(message = "El email es requerido")
        String correoInstitucional,

        @NotBlank(message = "El genero es requerido")
        String generoCodigo,

        String telefono
) {}
