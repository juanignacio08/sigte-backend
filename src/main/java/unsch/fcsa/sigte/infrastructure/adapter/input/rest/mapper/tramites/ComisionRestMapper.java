package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.tramites.ComisionCommand;
import unsch.fcsa.sigte.application.command.tramites.MiembroComisionCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionDetalleView;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionMiembroDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Comision;
import unsch.fcsa.sigte.domain.model.tramites.ComisionMiembro;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.ComisionMiembroRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.ComisionMiembroResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.ComisionRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.ComisionResponse;

@Component
public class ComisionRestMapper {

    public ComisionCommand toCreate(ComisionRequest r) {
        return new ComisionCommand(
                null, r.tesisId(), r.etapaCodigo(), r.fechaConformacion()
        );
    }

    public ComisionCommand toUpdate(Long id, ComisionRequest r) {
        return new ComisionCommand(
                id, r.tesisId(), r.etapaCodigo(), r.fechaConformacion()
        );
    }

    public ComisionResponse toResponse(Comision domain) {
        if (domain == null) return null;
        return ComisionResponse.builder()
                .id(domain.getId())
                .tesisId(domain.getTesisId())
                .etapaCodigo(domain.getEtapaCodigo())
                .fechaConformacion(domain.getFechaConformacion())
                .estado(domain.getEstado())
                .build();
    }

    public ComisionResponse fromViewToResponse(ComisionDetalleView domain) {
        if (domain == null) return null;
        return ComisionResponse.builder()
                .id(domain.getId())
                .tesisId(domain.getTesisId())
                .etapaCodigo(domain.getEtapaCodigo())
                .etapaDenominacion(domain.getEtapaDenominacion())
                .fechaConformacion(domain.getFechaConformacion())
                .estado(domain.getEstado())
                .build();
    }

    public MiembroComisionCommand toAgregar(ComisionMiembroRequest r) {
        return new MiembroComisionCommand(r.comisionId(), r.docenteId(), r.rolCodigo(), r.orden());
    }

    public ComisionMiembroResponse toMiembroDetailResponse(ComisionMiembroDetalleView d) {
        if (d == null) return null;
        return ComisionMiembroResponse.builder()
                .id(d.id())
                .docenteId(d.docenteId())
                .docenteNombre(d.docenteNombre())
                .correoInstitucional(d.correoInstitucional())
                .rolCodigo(d.rolCodigo())
                .rolDenominacion(d.rolDenominacion())
                .orden(d.orden())
                .fechaAlta(d.fechaAlta())
                .fechaBaja(d.fechaBaja())
                .motivoBaja(d.motivoBaja())
                .build();
    }

    public ComisionMiembroResponse toMiembroResponse(ComisionMiembro domain) {
        if (domain == null) return null;
        return ComisionMiembroResponse.builder()
                .id(domain.getId())
                .docenteId(domain.getDocenteId())
                .rolCodigo(domain.getRolCodigo())
                .orden(domain.getOrden())
                .fechaAlta(domain.getFechaAlta())
                .fechaBaja(domain.getFechaBaja())
                .motivoBaja(domain.getMotivoBaja())
                .build();
    }

}
