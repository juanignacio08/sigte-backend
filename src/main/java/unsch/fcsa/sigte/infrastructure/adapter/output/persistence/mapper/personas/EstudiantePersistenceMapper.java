package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.personas;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.personas.dto.EstudianteDetalleView;
import unsch.fcsa.sigte.domain.model.personas.Estudiante;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.EstudianteEntity;

@Component
public class EstudiantePersistenceMapper {

    private final MaestroPersistenceAdapter adapter;

    public EstudiantePersistenceMapper(MaestroPersistenceAdapter adapter) {
        this.adapter = adapter;
    }

    public Estudiante toDomain(EstudianteEntity entity) {
        if (entity == null) return null;

        return new Estudiante(
                entity.getId(),
                entity.getEscuela().getId(),
                entity.getCodigo(),
                entity.getNumeroDocumento(),
                entity.getNombres(),
                entity.getPrimerApellido(),
                entity.getSegundoApellido(),
                entity.getCorreoInstitucional(),
                entity.getGeneroCodigo(),
                entity.getTelefono(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }

    public EstudianteEntity toEntity(Estudiante domain) {
        if (domain == null) return null;

        EscuelaEntity escuela = EscuelaEntity.builder()
                .id(domain.getEscuelaId())
                .build();

        return EstudianteEntity.builder()
                .id(domain.getId())
                .escuela(escuela)
                .codigo(domain.getCodigo())
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

    public EstudianteDetalleView toEstudianteDetalleView(EstudianteEntity entity) {
        if (entity == null) return null;

        String genero = adapter.getItemByCode(entity.getGeneroCodigo()) != null ?
                adapter.getItemByCode(entity.getGeneroCodigo()).getDenominacion() : "NO REGISTRADO";

        return new EstudianteDetalleView(
                entity.getId(),
                entity.getCodigo(),
                entity.getEscuela().getId(),
                entity.getEscuela().getDenominacion(),
                entity.getNumeroDocumento(),
                entity.getNombres(),
                entity.getPrimerApellido(),
                entity.getSegundoApellido(),
                entity.getCorreoInstitucional(),
                entity.getGeneroCodigo(),
                genero,
                entity.getTelefono(),
                entity.getEstado()
        );
    }
}