package unsch.fcsa.sigte.application.command.personas;

public record EstudianteCommand(
        Long id,
        Long escuelaId,
        String codigo,
        String numeroDocumento,
        String nombres,
        String primerApellido,
        String segundoApellido,
        String correoInstitucional,
        String generoCodigo,
        String telefono
) {}