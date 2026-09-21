package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.DepartamentoResponse;

@Component
public class DepartamentoMapperExpose {

    public DepartamentoResponse toResponse(DepartamentoAcademico domain) {
        if(domain == null) return null;

        return DepartamentoResponse.builder()
                .id(domain.getId())
                .facultadId(domain.getFacultadId())
                .abreviatura(domain.getAbreviatura())
                .denominacion(domain.getDenominacion())
                .estado(domain.getEstado())
                .build();
    }
}
