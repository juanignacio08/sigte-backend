package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.time.LocalDate;

public record ResolutionProjectData(
        // Header
        String numeroResolucion,
        String anioResolucion,
        LocalDate fechaEmision,

        String tesistas,
        String generoGraduado,
        String generoAutores,
        String numeroExpediente,
        LocalDate fechaExpediente,
        String escuelaDenominacion,
        String tituloTesis,

        String asesores,
        String generoAsesorDe,
        String generoAsesorA,
        String generoSustantivoAsesor,

        String presidente,
        String generoPresidenteDe,
        String jurados,
        String generoJurados,

        String numeroMemo,
        String anioMemo,
        LocalDate fechaMemo,
        LocalDate fechaDictamen,

        String tituloAprobado,

        String rutaPlantilla
) {}