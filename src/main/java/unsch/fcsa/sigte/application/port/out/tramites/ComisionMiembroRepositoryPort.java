package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.application.port.out.tramites.dto.ComisionMiembroDetalleView;
import unsch.fcsa.sigte.domain.model.tramites.ComisionMiembro;

import java.util.List;
import java.util.Optional;

public interface ComisionMiembroRepositoryPort {
    ComisionMiembro guardar(ComisionMiembro miembro);
    List<ComisionMiembro> guardarTodo(List<ComisionMiembro> miembros);
    Optional<ComisionMiembro> buscarPorId(Long id);
    List<ComisionMiembro> listarActivosPorComision(Long comisionId);
    List<ComisionMiembroDetalleView> listarDetallePorComision(Long comisionId);
    boolean existeDocenteActivoEnComision(Long comisionId, Long docenteId);
    boolean existeRolSingularActivo(Long comisionId, String rolCodigo);
    boolean existePorId(Long id);

    void eliminar(Long id);
}
