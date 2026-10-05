package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalResponse;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record EstudianteResponse(
        Long id,
        String codigo,
        EscuelaProfesionalResponse escuela,
        String numeroDocumento,
        String nombres,
        String primerApellido,
        String segundoApellido,
        String correoInstitucional,
        String generoCodigo,
        String generoDenominacion,
        String telefono,
        Integer estado,
        Boolean eliminado
) {}