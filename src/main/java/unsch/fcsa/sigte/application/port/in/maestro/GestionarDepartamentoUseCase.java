package unsch.fcsa.sigte.application.port.in.maestro;

import unsch.fcsa.sigte.application.command.maestro.DepartamentoCommand;
import unsch.fcsa.sigte.application.port.out.maestro.projections.DepartamentoConTotalEscuelasView;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;

import java.util.List;

public interface GestionarDepartamentoUseCase {

    DepartamentoAcademico crearDepartamento(DepartamentoCommand command);
    DepartamentoAcademico actualizarDepartamento(DepartamentoCommand command);
    List<DepartamentoConTotalEscuelasView> listarTodosPorFacultad(Long facultadId);
    DepartamentoAcademico obtenerPorId(Long id);
    DepartamentoAcademico actualizarDepartamento(Long id, String abreviatura, String denominacion);

    void eliminarDepartamento(Long id);

}
