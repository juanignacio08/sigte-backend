package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.tramites.ActualizarExpedienteCommand;
import unsch.fcsa.sigte.application.command.tramites.RegistrarExpedienteCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteArchivoDetalleView;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;
import unsch.fcsa.sigte.domain.model.tramites.ExpedienteTesis;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.ActualizarExpedienteRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.ExpedienteResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.RegistrarExpedienteRequest;

@Component
public class ExpedienteRestMapper {
    public RegistrarExpedienteCommand toCreate(RegistrarExpedienteRequest request) {
        return new RegistrarExpedienteCommand(
                request.tesisId(),
                request.numeroExpediente(),
                request.etapaCodigo(),
                request.numeroRevision(),
                request.fechaRegistro()
        );
    }

    public ActualizarExpedienteCommand toUpdate(Long id, ActualizarExpedienteRequest request) {
        return new ActualizarExpedienteCommand(
                id,
                request.numeroExpediente(),
                request.etapaCodigo(),
                request.numeroRevision(),
                request.fechaIngreso()
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
                .id(domain.getId())
                .tesisId(domain.getTesisId())
                .numeroExpediente(domain.getNumeroExpediente())
                .etapaCodigo(domain.getEtapaCodigo())
                .etapaDenominacion(domain.getEtapaDenominacion())
                .numRevision(domain.getNumeroRevision())
                .fechaAsociacion(domain.getFechaIngreso())
                .estado(domain.getEstado())
                .build();
    }

    public ExpedienteResponse toResponseDetail(ExpedienteDetalleView domain) {
        if (domain == null) return null;

        return ExpedienteResponse.builder()
                .id(domain.getId())
                .tesisId(domain.getTesisId())
                .numeroExpediente(domain.getNumeroExpediente())
                .etapaCodigo(domain.getEtapaCodigo())
                .etapaDenominacion(domain.getEtapaDenominacion())
                .numRevision(domain.getNumeroRevision())
                .fechaAsociacion(domain.getFechaIngreso())
                .estado(domain.getEstado())
                .archivos(domain.getArchivos())
                .build();
    }

    public ExpedienteArchivoDetalleView toArchivoDetalle(ExpedienteArchivo domain) {
        if (domain == null) return null;

        return new ExpedienteArchivoDetalleView(
                domain.getId(),
                domain.getNombreArchivo(),
                domain.getArchivoUrl(),
                domain.getTamanioBytes()
        );
    }

}
