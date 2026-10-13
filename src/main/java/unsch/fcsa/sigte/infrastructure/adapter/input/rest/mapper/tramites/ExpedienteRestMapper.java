package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.tramites.RegistrarExpedienteCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.ExpedienteResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.RegistrarExpedienteRequest;

@Component
public class ExpedienteRestMapper {
    public RegistrarExpedienteCommand toCreate(RegistrarExpedienteRequest request) {
        return new RegistrarExpedienteCommand(
                request.tesisId(),
                request.numeroExpediente(),
                request.etapaCodigo(),
                request.numeroRevision()
        );
    }

    public ExpedienteResponse toResponse(ExpedienteTesis domain) {
        if (domain == null) return null;

        return ExpedienteResponse.builder()
                .id(domain.getId())
                .tesisId(domain.getTesisId())
                .numeroExpediente(domain.getNumeroExpediente())
                .etapaCodigo(domain.getEtapaCodigo())
                .numRevision(domain.getNumeroRevision())
                .fechaAsociacion(domain.getFechaIngreso())
                .estado(domain.getEstado())
                .build();
    }

    public ExpedienteResponse toResponseWithMasterTable(ExpedienteDetalleView domain) {
        if (domain == null) return null;

        return ExpedienteResponse.builder()
                .id(domain.id())
                .tesisId(domain.tesisId())
                .numeroExpediente(domain.numeroExpediente())
                .etapaCodigo(domain.etapaCodigo())
                .etapaDenominacion(domain.etapaDenominacion())
                .numRevision(domain.numeroRevision())
                .fechaAsociacion(domain.fechaIngreso())
                .estado(domain.estado())
                .build();
    }

    public ExpedienteResponse toResponseDetail(ExpedienteDetalleView domain) {
        if (domain == null) return null;

        return ExpedienteResponse.builder()
                .id(domain.id())
                .tesisId(domain.tesisId())
                .numeroExpediente(domain.numeroExpediente())
                .etapaCodigo(domain.etapaCodigo())
                .etapaDenominacion(domain.etapaDenominacion())
                .numRevision(domain.numeroRevision())
                .fechaAsociacion(domain.fechaIngreso())
                .estado(domain.estado())
                .archivos(domain.archivos())
                .build();
    }

}
