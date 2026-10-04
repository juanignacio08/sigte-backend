package unsch.fcsa.sigte.application.port.out.personas.dto;

public record DocenteFiltro(
        Long facultadId,
        Long departamentoAcademicoId,
        Long escuelaId,
        Integer estado,
        String generoCodigo,
        String numeroDocumento,
        String correo
) {}
