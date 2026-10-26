package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionMiembroDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ComisionMiembro;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro.MaestroPersistenceAdapter;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.personas.DocenteEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.ComisionMiembroEntity;

@Component
public class ComisionMiembroPersistenceMapper {

    private final MaestroPersistenceAdapter maestroAdapter;

    public ComisionMiembroPersistenceMapper(MaestroPersistenceAdapter maestroAdapter) {
        this.maestroAdapter = maestroAdapter;
    }

    public ComisionMiembroEntity toEntity(ComisionMiembro domain) {
        if (domain == null) return null;
        return ComisionMiembroEntity.builder()
                .id(domain.getId())
                .comisionId(domain.getComisionId())
                .rolCodigo(domain.getRolCodigo())
                .orden(domain.getOrden())
                .fechaAlta(domain.getFechaAlta())
                .fechaBaja(domain.getFechaBaja())
                .motivoBaja(domain.getMotivoBaja())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();
    }

    public ComisionMiembro toDomain(ComisionMiembroEntity entity) {
        if (entity == null) return null;
        return new ComisionMiembro(
                entity.getId(),
                entity.getComisionId(),
                entity.getDocente().getId(),
                entity.getRolCodigo(),
                entity.getOrden(),
                entity.getFechaAlta(),
                entity.getFechaBaja(),
                entity.getMotivoBaja(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }

    public ComisionMiembroDetalleView toDetalleView(ComisionMiembroEntity entity) {
        if (entity == null) return null;
        String rol = maestroAdapter.getItemByCode(entity.getRolCodigo()) != null
                ? maestroAdapter.getItemByCode(entity.getRolCodigo()).getDenominacion()
                : "NO REGISTRADO";

        DocenteEntity docente = entity.getDocente();

        String nombre = docente != null
                ? docente.getNombres() + " " + docente.getPrimerApellido()
                + (docente.getSegundoApellido() != null ? " " + docente.getSegundoApellido() : "")
                : "DOCENTE NO ENCONTRADO";

        String correo = docente != null ? docente.getCorreoInstitucional() : null;

        return ComisionMiembroDetalleView.builder()
                .id(entity.getId())
                .docenteId(entity.getDocente().getId())
                .docenteNombre(nombre)
                .correoInstitucional(correo)
                .rolCodigo(entity.getRolCodigo())
                .rolDenominacion(rol)
                .orden(entity.getOrden())
                .fechaAlta(entity.getFechaAlta())
                .fechaBaja(entity.getFechaBaja())
                .motivoBaja(entity.getMotivoBaja())
                .build();
    }

}
