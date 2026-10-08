package unsch.fcsa.sigte.application.port.out.tramites.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record TesisDetalleView(
        UUID id,
        Long escuelaId,
        String escuelaDenominacion,
        String titulo,
        String tituloDictaminado,
        String estadoCodigo,
        String estadoDenominacion,
        OffsetDateTime fechaRegistro,
        List<AutorView> autores,
        ExpedienteView expedienteActivo
) {
    public record AutorView(
            Long estudianteId,
            String codigoEstudiante,
            String numeroDocumento,
            String nombres,
            String primerApellido,
            String segundoApellido,
            String correoInstitucional
    ) {}

    public record ExpedienteView(
            Long id,
            String numeroExpediente,
            String etapaCodigo,
            String etapaDenominacion,
            Integer numeroRevision,
            OffsetDateTime fechaIngreso
    ) {}
}