package unsch.fcsa.sigte.application.command.personas;

public record EstudianteFiltro(
        Long facultadId,
        Long escuelaId,
        String codigo,
        String numeroDocumento,
        String nombre,
        String email,
        String generoCodigo
) {}