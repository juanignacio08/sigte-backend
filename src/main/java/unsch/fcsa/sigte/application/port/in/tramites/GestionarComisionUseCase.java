package unsch.fcsa.sigte.application.port.in.tramites;

import unsch.fcsa.sigte.application.command.tramites.ComisionCommand;
import unsch.fcsa.sigte.application.command.tramites.MiembroComisionCommand;
import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.Comision;
import unsch.fcsa.sigte.domain.model.tramites.ComisionMiembro;

import java.util.List;
import java.util.UUID;

public interface GestionarComisionUseCase {
    Comision registrar(ComisionCommand command);
    Comision actualizar(ComisionCommand command);

    ComisionMiembro agregarMienbro(MiembroComisionCommand command);

    List<ComisionDetalleView> listarPorTesis(UUID tesisId);
    void eliminar(Long id);
}
