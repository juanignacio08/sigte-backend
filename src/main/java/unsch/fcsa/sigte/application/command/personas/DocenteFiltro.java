package unsch.fcsa.sigte.application.command.personas;

public record DocenteFiltro(
        Long facultadId,
        Long departamentoAcademicoId,
        Long escuelaId,
        Integer estado,
        String generoCodigo,
        String numeroDocumento,
        String correo
) {}
