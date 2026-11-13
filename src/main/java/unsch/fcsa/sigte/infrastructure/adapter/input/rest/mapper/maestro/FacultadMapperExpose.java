package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.Facultad;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.FacultadResponse;

@Component
public class FacultadMapperExpose {

    public FacultadResponse toResponse(Facultad facultad) {
        if (facultad == null) return null;

        return FacultadResponse.builder()
                .id(facultad.getId())
                .abreviatura(facultad.getAbreviatura())
                .denominacion(facultad.getDenominacion())
                .estado(facultad.getEstado())
                .build();
    }

}
