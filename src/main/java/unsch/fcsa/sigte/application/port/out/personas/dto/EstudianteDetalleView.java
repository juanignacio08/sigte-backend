package unsch.fcsa.sigte.application.port.out.personas.dto;

public record EstudianteDetalleView(
        Long id,
        String codigo,
        Long escuelaId,
        String escuelaDenominacion,
        String numeroDocumento,
        String nombres,
        String primerApellido,
        String segundoApellido,
        String correoInstitucional,
        String generoCodigo,
        String generoDenominacion,
        String telefono,
        Integer estado
) {}