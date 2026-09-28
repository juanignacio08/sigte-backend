package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.DepartamentoConTotalEscuelasView;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.DepartamentoDetalle;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.DepartamentoResponse;

@Component
@RequiredArgsConstructor
public class DepartamentoMapperExpose {

    private final EscuelaProfesionalMapperExpose mapperExpose;

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

    public DepartamentoResponse fromProjectionToResponse(DepartamentoConTotalEscuelasView projection) {
        if(projection == null) return null;

        return DepartamentoResponse.builder()
                .id(projection.getId())
                .facultadId(projection.getFacultadId())
                .abreviatura(projection.getAbreviatura())
                .denominacion(projection.getDenominacion())
                .estado(projection.getEstado())
                .totalEscuelas(projection.getTotalEscuelas())
                .build();
    }

    public DepartamentoResponse fromDetalleToResponse(DepartamentoDetalle detalle) {
        if(detalle == null) return null;

        return DepartamentoResponse.builder()
                .id(detalle.id())
                .facultadId(detalle.facultadId())
                .abreviatura(detalle.abreviatura())
                .denominacion(detalle.denominacion())
                .estado(detalle.estado())
                .escuelas(
                        detalle.escuelas().stream()
                                .map(mapperExpose::toResponse)
                                .toList()
                )
                .build();
    }
}
