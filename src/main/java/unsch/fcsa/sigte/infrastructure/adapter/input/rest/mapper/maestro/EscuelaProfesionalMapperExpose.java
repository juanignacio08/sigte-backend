package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalResponse;

@Component
public class EscuelaProfesionalMapperExpose {

    public EscuelaProfesionalResponse toResponse(EscuelaProfesional domain) {
        if(domain == null) return null;

        return EscuelaProfesionalResponse.builder()
                .id(domain.getId())
                .departamentoId(domain.getDepartamentoAcademicoId())
                .codigo(domain.getCodigo())
                .abreviatura(domain.getAbreviatura())
                .denominacion(domain.getDenominacion())
                .plantillaMemo(domain.getPlantillaMemorando())
                .plantillaRes(domain.getPlantillaResolucion())
                .build();
    }

}
