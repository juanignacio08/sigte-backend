package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ComisionMiembroResponse(
        Long id,
        Long docenteId,
        String docenteNombre,
        String correoInstitucional,
        String rolCodigo,
        String rolDenominacion,
        Integer orden,
        LocalDate fechaAlta,
        LocalDate fechaBaja,
        String motivoBaja
) {}
