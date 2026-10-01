package unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.DepartamentoResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalResponse;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DocenteResponse(
        Long id,
        String codigo,
        EscuelaProfesionalResponse escuela,
        DepartamentoResponse departamento,
        String tipoDocumentoCodigo,
        String tipoDocumentoDenominacion,
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
