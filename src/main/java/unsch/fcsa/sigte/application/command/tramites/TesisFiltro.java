package unsch.fcsa.sigte.application.command.tramites;

public record TesisFiltro(
        Long facultadId,
        Long escuelaId,
        String estadoCodigo,
        String textoBusqueda
) {}