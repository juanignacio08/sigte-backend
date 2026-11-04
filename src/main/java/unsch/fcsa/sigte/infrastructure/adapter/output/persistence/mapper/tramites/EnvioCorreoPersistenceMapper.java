package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.tramites;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.tramites.EnvioCorreo;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.tramites.EnvioCorreoEntity;

@Component
public class EnvioCorreoPersistenceMapper {

    public EnvioCorreoEntity toEntity(EnvioCorreo domain) {
        if (domain == null) return null;

        return EnvioCorreoEntity.builder()
                .id(domain.getId())
                .expedienteId(domain.getExpedienteId())
                .asunto(domain.getAsunto())
                .cuerpo(domain.getCuerpo())
                .destinatarios(domain.getDestinatariosJson())
                .adjuntos(domain.getAdjuntosJson())
                .fechaEnvio(domain.getFechaEnvio())
                .errorMensaje(domain.getErrorMensaje())
                .estado(domain.getEstado())
                .eliminado(domain.isEliminado())
                .build();
    }

    public EnvioCorreo toDomain(EnvioCorreoEntity entity) {
        if (entity == null) return null;

        return new EnvioCorreo(
                entity.getId(),
                entity.getExpedienteId(),
                entity.getAsunto(),
                entity.getCuerpo(),
                entity.getDestinatarios(),
                entity.getAdjuntos(),
                entity.getFechaEnvio(),
                entity.getErrorMensaje(),
                entity.getEstado(),
                entity.getEliminado()
        );
    }
}