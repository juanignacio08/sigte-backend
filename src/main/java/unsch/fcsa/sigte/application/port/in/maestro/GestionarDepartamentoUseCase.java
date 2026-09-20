package unsch.fcsa.sigte.application.port.in.maestro;

import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;

import java.util.List;

public interface GestionarDepartamentoUseCase {

    DepartamentoAcademico crearDepartamento(Long facultadId, String abreviatura, String denominacion);

    DepartamentoAcademico obtenerPorId(Long id);

    List<DepartamentoAcademico> listarTodos();

    DepartamentoAcademico actualizarDepartamento(Long id, String abreviatura, String denominacion);

    void eliminarDepartamento(Long id);

}
