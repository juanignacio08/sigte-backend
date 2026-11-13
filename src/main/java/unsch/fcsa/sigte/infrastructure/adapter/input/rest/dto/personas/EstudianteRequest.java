package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EstudianteRequest(
        @NotNull(message = "El ID de la escuela es obligatorio")
        Long escuelaId,

        @Size(max = 10, message = "El código no debe superar los 10 caracteres")
        String codigo,

        @Size(max = 15, message = "El número de documento no debe superar los 15 caracteres")
        String numeroDocumento,

        @NotBlank(message = "Los nombres son obligatorios")
        @Size(max = 100, message = "Los nombres no deben superar los 100 caracteres")
        String nombres,

        @NotBlank(message = "El primer apellido es obligatorio")
        @Size(max = 100, message = "El primer apellido no debe superar los 100 caracteres")
        String primerApellido,

        @NotBlank(message = "El segundo apellido es obligatorio")
        @Size(max = 100, message = "El segundo apellido no debe superar los 100 caracteres")
        String segundoApellido,

        @NotBlank(message = "El correo institucional es obligatorio")
        @Email(message = "El formato del correo electrónico es inválido")
        @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
        String correoInstitucional,

        @NotBlank(message = "El código de género es obligatorio")
        @Size(min = 6, max = 6, message = "El código de género debe tener exactamente 6 caracteres")
        String generoCodigo,

        @Size(max = 15, message = "El teléfono no debe superar los 15 caracteres")
        String telefono
) {}