package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.tramites.TesisCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.AutorView;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ExpedienteDetalleView;
import unsch.fcsa.sigte.application.port.out.tramites.dto.TesisDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Tesis;
import unsch.fcsa.sigte.domain.model.tramites.TesisAutor;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.RegistrarTesisRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.tramites.TesisResponse;

import java.util.ArrayList;
import java.util.List;

@Component
public class TesisRestMapper {
    public TesisCommand toCommandCreate(RegistrarTesisRequest request) {
        return TesisCommand.create(
                request.escuelaId(),
                request.titulo(),
                request.estudiantesIds(),
                request.numeroExpediente(),
                request.etapaTesisCodigo(),
                request.fechaIngreso(),
                request.numeroRevision()
        );
    }

    public TesisResponse toResponse(Tesis domain) {
        if (domain == null) return null;

        List<TesisResponse.TesisAutorResponse> autores = new ArrayList<>();

        if (domain.getAutores() != null && !domain.getAutores().isEmpty()) {
            autores = domain.getAutores().stream()
                    .map(this::toAutorResponse)
                    .toList();
        }

        return TesisResponse.builder()
                .id(domain.getId())
                .escuelaId(domain.getEscuelaId())
                .titulo(domain.getTitulo())
                .tituloDictaminado(domain.getTituloDictaminado())
                .estadoCodigo(domain.getEstadoCodigo())
                .fechaRegistro(domain.getFechaRegistro())
                .autores(autores)
                .build();
    }

    public TesisResponse toResponseDetail(TesisDetalleView domain) {
        if (domain == null) return null;

        List<TesisResponse.TesisAutorResponse> autores = new ArrayList<>();

        if (domain.getAutores() != null && !domain.getAutores().isEmpty()) {
            autores = domain.getAutores().stream()
                    .map(this::toAutorResponseDetail)
                    .toList();
        }

        TesisResponse.ExpedienteTesisResponse expedienteTesis = toExpedienteTesisResponseDetail(domain.getExpedienteActual());

        return TesisResponse.builder()
                .id(domain.getId())
                .escuelaId(domain.getEscuelaId())
                .escuelaDenominacion(domain.getEscuelaDenominacion())
                .titulo(domain.getTitulo())
                .tituloDictaminado(domain.getTituloDictaminado())
                .estadoCodigo(domain.getEstadoCodigo())
                .estadoDenominacion(domain.getEstadoDenominacion())
                .fechaRegistro(domain.getFechaRegistro())
                .autores(autores)
                .expedienteActivo(expedienteTesis)
                .build();
    }

    public TesisResponse.TesisAutorResponse toAutorResponse(TesisAutor domain) {
        if (domain == null) return null;

        return TesisResponse.TesisAutorResponse.builder()
                .id(domain.getId())
                .tesisId(domain.getTesisId())
                .estudianteId(domain.getEstudianteId())
                .fechaAsociacion(domain.getFechaAsociacion())
                .estado(domain.getEstado())
                .build();
    }

    public TesisResponse.TesisAutorResponse toAutorResponseDetail(AutorView domain) {
        if (domain == null) return null;

        return TesisResponse.TesisAutorResponse.builder()
                .id(domain.id())
                .estudianteId(domain.estudianteId())
                .estudianteNombres(domain.nombres())
                .estudiantePrimerApellido(domain.primerApellido())
                .estudianteSegundoApellido(domain.segundoApellido())
                .estudianteDNI(domain.numeroDocumento())
                .estudianteCorreo(domain.correoInstitucional())
                .fechaAsociacion(domain.fechaAsociacion())
                .estado(domain.estado())
                .build();
    }

    public TesisResponse.ExpedienteTesisResponse toExpedienteTesisResponseDetail(ExpedienteDetalleView domain) {
        if (domain == null) return null;

        return TesisResponse.ExpedienteTesisResponse.builder()
                .id(domain.getId())
                .tesisId(domain.getTesisId())
                .numeroExpediente(domain.getNumeroExpediente())
                .etapaCodigo(domain.getEtapaCodigo())
                .etapaDenominacion(domain.getEtapaDenominacion())
                .numRevision(domain.getNumeroRevision())
                .fechaAsociacion(domain.getFechaIngreso())
                .estadoCodigo(domain.getEstadoCodigo())
                .estadoDenominacion(domain.getEstadoDenominacion())
                .estado(domain.getEstado())
                .build();
    }
}
