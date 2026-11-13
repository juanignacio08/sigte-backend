package unsch.fcsa.sigte.application.port.out.tramites.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ComisionMiembroDetalleView(
        Long id,
        Long docenteId,
        String docenteNombre,
        String correoInstitucional,
        String rolCodigo,
        String rolDenominacion,
        String generoCodigo,
        Integer orden,
        LocalDate fechaAlta,
        LocalDate fechaBaja,
        String motivoBaja
) {}