package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.personas;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.personas.dto.DocenteDetalleView;
import unsch.fcsa.sigte.domain.model.maestro.Maestro;
import unsch.fcsa.sigte.domain.model.personas.Docente;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.DocenteEntity;

@Component
public class DocentePersistenceMapper {

    private final MaestroPersistenceAdapter adapter;

    public DocentePersistenceMapper(MaestroPersistenceAdapter adapter) {
        this.adapter = adapter;
    }

    public Docente toDomain(DocenteEntity entity) {
        if (entity == null) return null;

        return new Docente(
                entity.getId(),
                entity.getCodigo(),
                entity.getEscuela().getId(),
                entity.getDepartamentoAcademico().getId(),
                entity.getTipoDocumentoCodigo(),
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

    public DocenteEntity toEntity(Docente domain) {
        if (domain == null) return null;

        EscuelaEntity escuela = EscuelaEntity.builder()
                .id(domain.getEscuelaId())
                .build();

        DepartamentoEntity departamento = DepartamentoEntity.builder()
                .id(domain.getDepartamentoAcademicoId())
                .build();

        return DocenteEntity.builder()
                .id(domain.getId())
                .codigo(domain.getCodigo())
                .escuela(escuela)
                .departamentoAcademico(departamento)
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

    public DocenteDetalleView toDocenteDetalleView(DocenteEntity entity) {
        if (entity == null) return null;

        String genero = adapter.getItemByCode(entity.getGeneroCodigo()) != null ?
                adapter.getItemByCode(entity.getGeneroCodigo()).getDenominacion() : "NO REGISTRADO";

        String tipoDocumento = adapter.getItemByCode(entity.getTipoDocumentoCodigo()) != null ?
                adapter.getItemByCode(entity.getTipoDocumentoCodigo()).getDenominacion() : "NO REGISTRADO";

        return new DocenteDetalleView(
                entity.getId(),
                entity.getCodigo(),
                entity.getEscuela().getId(),
                entity.getEscuela().getDenominacion(),
                entity.getDepartamentoAcademico().getId(),
                entity.getDepartamentoAcademico().getDenominacion(),
                entity.getTipoDocumentoCodigo(),
                tipoDocumento,
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
