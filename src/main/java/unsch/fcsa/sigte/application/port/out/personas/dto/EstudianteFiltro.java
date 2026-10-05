package unsch.fcsa.sigte.application.port.out.personas.dto;

public record EstudianteFiltro(
        Long facultadId,
        Long escuelaId,
        String codigo,
        String numeroDocumento,
        String nombre,
        String email,
        String generoCodigo
) {}