package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.Maestro;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.MasterEntity;

@Component
public class MasterMapperOut {

    public Maestro toDomain(MasterEntity entity) {
        if (entity == null) return null;

        return new Maestro(
                entity.getId(),
                entity.getTableCode(),
                entity.getItemCode(),
                entity.getCode(),
                entity.getDenomination(),
                entity.getIsSystem(),
                entity.getState(),
                entity.getIsDeleted()
        );
    }

    public MasterEntity toEntity(Maestro domain) {
        if (domain == null) return null;

        return MasterEntity.builder()
                .id(domain.getId())
                .tableCode(domain.getCodigoTabla())
                .itemCode(domain.getCodigoItem())
                .code(domain.getCodigo())
                .denomination(domain.getDenominacion())
                .isSystem(domain.getEsSistema())
                .state(domain.getEstado())
                .isDeleted(domain.isEliminado())
                .build();
    }

}
