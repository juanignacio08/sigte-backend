package unsch.fcsa.sigte.application.port.in.maestro;

import unsch.fcsa.sigte.application.command.maestro.DepartamentoCommand;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.DepartamentoConTotalEscuelasView;
import unsch.fcsa.sigte.application.port.out.maestro.dtos.DepartamentoDetalle;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;

import java.util.List;

public interface GestionarDepartamentoUseCase {

    DepartamentoAcademico crearDepartamento(DepartamentoCommand command);
    DepartamentoAcademico actualizarDepartamento(DepartamentoCommand command);
    List<DepartamentoConTotalEscuelasView> listarTodosPorFacultad(Long facultadId);
    DepartamentoAcademico obtenerPorId(Long id);
    DepartamentoDetalle obtenerADetallePorId(Long id);
    List<DepartamentoAcademico> listarActivosPorFacultad(Long facultadId);
    DepartamentoAcademico activarDepartamento(Long id);
    DepartamentoAcademico desactivarDepartamento(Long id);
    void eliminarDepartamento(Long id);

}
