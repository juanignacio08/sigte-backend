package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.EscuelaProfesionalProjection;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.DepartamentoResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalResponse;

@Component
public class EscuelaProfesionalMapperExpose {

    public EscuelaProfesionalResponse toResponse(EscuelaProfesional domain) {
        if(domain == null) return null;

        return EscuelaProfesionalResponse.builder()
                .id(domain.getId())
                .departamento(DepartamentoResponse.builder()
                        .id(domain.getDepartamentoAcademicoId())
                        .build())
                .codigo(domain.getCodigo())
                .abreviatura(domain.getAbreviatura())
                .denominacion(domain.getDenominacion())
                .plantillaMemo(domain.getPlantillaMemorando())
                .plantillaRes(domain.getPlantillaResolucion())
                .build();
    }

    public EscuelaProfesionalResponse fromProjectionToResponse(EscuelaProfesionalProjection domain) {
        if(domain == null) return null;

        return EscuelaProfesionalResponse.builder()
                .id(domain.getId())
                .departamento(DepartamentoResponse.builder()
                        .id(domain.getDepartamentoId())
                        .denominacion(domain.getDepartamentoDenominacion())
                        .build())
                .codigo(domain.getCodigo())
                .abreviatura(domain.getAbreviatura())
                .denominacion(domain.getDenominacion())
                .plantillaMemo(domain.getPlantillaMemorando())
                .plantillaRes(domain.getPlantillaResolucion())
                .build();
    }

}
