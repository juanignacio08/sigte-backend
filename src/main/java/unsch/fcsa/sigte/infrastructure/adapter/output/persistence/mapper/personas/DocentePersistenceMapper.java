package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.personas;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.personas.Docente;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.DepartamentoEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.EscuelaEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.DocenteEntity;

@Component
public class DocentePersistenceMapper {

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
}
