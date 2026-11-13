package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.personas;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.command.personas.EstudianteCommand;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteDetalleView;
import unsch.fcsa.sigte.domain.model.personas.Estudiante;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.EscuelaProfesionalResponse;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.EstudianteRequest;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.personas.EstudianteResponse;

@Component
public class EstudianteRestMapper {

    public EstudianteCommand toCommandCreate(EstudianteRequest request) {
        return new EstudianteCommand(
                null,
                request.escuelaId(),
                request.codigo(),
                request.numeroDocumento(),
                request.nombres(),
                request.primerApellido(),
                request.segundoApellido(),
                request.correoInstitucional(),
                request.generoCodigo(),
                request.telefono()
        );
    }

    public EstudianteCommand toCommandUpdate(Long id, EstudianteRequest request) {
        return new EstudianteCommand(
                id,
                request.escuelaId(),
                request.codigo(),
                request.numeroDocumento(),
                request.nombres(),
                request.primerApellido(),
                request.segundoApellido(),
                request.correoInstitucional(),
                request.generoCodigo(),
                request.telefono()
        );
    }

    public EstudianteResponse toResponse(Estudiante domain) {
        if (domain == null) return null;

        EscuelaProfesionalResponse escuela = EscuelaProfesionalResponse.builder()
                .id(domain.getEscuelaId())
                .build();

        return EstudianteResponse.builder()
                .id(domain.getId())
                .codigo(domain.getCodigo())
                .escuela(escuela)
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

    public EstudianteResponse fromViewToResponse(EstudianteDetalleView view) {
        if (view == null) return null;

        EscuelaProfesionalResponse escuela = EscuelaProfesionalResponse.builder()
                .id(view.escuelaId())
                .denominacion(view.escuelaDenominacion())
                .build();

        return EstudianteResponse.builder()
                .id(view.id())
                .codigo(view.codigo())
                .escuela(escuela)
                .numeroDocumento(view.numeroDocumento())
                .nombres(view.nombres())
                .primerApellido(view.primerApellido())
                .segundoApellido(view.segundoApellido())
                .correoInstitucional(view.correoInstitucional())
                .generoCodigo(view.generoCodigo())
                .generoDenominacion(view.generoDenominacion())
                .telefono(view.telefono())
                .estado(view.estado())
                .build();
    }
}