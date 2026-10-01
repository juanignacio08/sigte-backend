package unsch.fcsa.sigte.infrastructure.adapter.input.rest.mapper.maestro;

import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.domain.model.maestro.Maestro;
import unsch.fcsa.sigte.infrastructure.adapter.input.rest.dto.maestro.MaestroResponse;

@Component
public class MasterMapperExpose {

    public MaestroResponse toResponse(Maestro domain) {
        if(domain == null) return null;

        return MaestroResponse.builder()
                .id(domain.getId())
                .tableCode(domain.getCodigoTabla())
                .itemCode(domain.getCodigoItem())
                .code(domain.getCodigo())
                .denomination(domain.getDenominacion())
                .isSystem(domain.getEsSistema())
                .state(domain.getEstado())
                .build();
    }

}
