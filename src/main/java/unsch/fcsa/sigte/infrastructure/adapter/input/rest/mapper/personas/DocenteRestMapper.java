package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.personas;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.personas.DocenteCommand;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteDetalleView;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteMetricasProjection;
import unsch.fcsa.sigte.domain.model.personas.Docente;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.DepartamentoResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.DocenteMetricaResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.DocenteRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.DocenteResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class DocenteRestMapper {

    public DocenteCommand toDocenteCommandCreate(DocenteRequest request) {
        return DocenteCommand.create(
                request.codigo(),
                request.escuelaId(),
                request.departamentoAcademicoId(),
                request.tipoDocumentoCodigo(),
                request.numeroDocumento(),
                request.nombres(),
                request.primerApellido(),
                request.segundoApellido(),
                request.correoInstitucional(),
                request.generoCodigo(),
                request.telefono()
        );
    }

    public DocenteCommand toDocenteCommandUpdate(Long id, DocenteRequest request) {
        return DocenteCommand.update(
                id,
                request.codigo(),
                request.escuelaId(),
                request.departamentoAcademicoId(),
                request.tipoDocumentoCodigo(),
                request.numeroDocumento(),
                request.nombres(),
                request.primerApellido(),
                request.segundoApellido(),
                request.correoInstitucional(),
                request.generoCodigo(),
                request.telefono()
        );
    }

    public DocenteResponse toResponse(Docente domain) {
        return DocenteResponse.builder()
                .id(domain.getId())
                .codigo(domain.getCodigo())
                .departamento(DepartamentoResponse.builder()
                        .id(domain.getDepartamentoAcademicoId())
                        .build())
                .escuela(EscuelaProfesionalResponse.builder()
                        .id(domain.getEscuelaId())
                        .build())
                .tipoDocumentoCodigo(domain.getTipoDocumentoCodigo())
                .numeroDocumento(domain.getNumeroDocumento())
                .nombres(domain.getNombres())
                .primerApellido(domain.getPrimerApellido())
                .segundoApellido(domain.getSegundoApellido())
                .correoInstitucional(domain.getCorreoInstitucional())
                .generoCodigo(domain.getGeneroCodigo())
                .telefono(domain.getTelefono())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();
    }

    public DocenteResponse fromDocenteViewToResponse(DocenteDetalleView domain) {
        return DocenteResponse.builder()
                .id(domain.id())
                .codigo(domain.codigo())
                .departamento(DepartamentoResponse.builder()
                        .id(domain.departamentoAcademicoId())
                        .denominacion(domain.departamentoDenominacion())
                        .build())
                .escuela(EscuelaProfesionalResponse.builder()
                        .id(domain.escuelaId())
                        .denominacion(domain.escuelaDenominacion())
                        .build())
                .tipoDocumentoCodigo(domain.tipoDocumentoCodigo())
                .tipoDocumentoDenominacion(domain.tipoDocumentoDenominacion())
                .numeroDocumento(domain.numeroDocumento())
                .nombres(domain.nombres())
                .primerApellido(domain.primerApellido())
                .segundoApellido(domain.segundoApellido())
                .correoInstitucional(domain.correoInstitucional())
                .generoCodigo(domain.generoCodigo())
                .generoDenominacion(domain.generoDenominacion())
                .telefono(domain.telefono())
                .estado(domain.estado())
                .build();
    }

    public DocenteMetricaResponse toMetrics(DocenteMetricasProjection projection) {
        return DocenteMetricaResponse.builder()
                .total(projection.getTotal())
                .totalActivos(projection.getTotalActivos())
                .porcentajeActivos(calcularPorcentaje(projection.getTotal(), projection.getTotalActivos()))
                .totalInactivos(projection.getTotalInactivos())
                .porcentajeInactivos(calcularPorcentaje(projection.getTotal(), projection.getTotalInactivos()))
                .build();
    }

    private Double calcularPorcentaje(Integer total, Integer parte) {
        if (total == null || parte == null || total == 0) {
            return 0.0;
        }

        double result = ((double) parte / total) * 100;

        BigDecimal bd = BigDecimal.valueOf(result);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}
