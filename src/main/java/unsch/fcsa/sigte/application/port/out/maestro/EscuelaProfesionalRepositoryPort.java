package unsch.fcsa.sigte.application.port.out.maestro;

import unsch.fcsa.sigte.application.port.out.maestro.dtos.EscuelaProfesionalProjection;
import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;

import java.util.List;
import java.util.Optional;

public interface EscuelaProfesionalRepositoryPort {
    EscuelaProfesional guardar(EscuelaProfesional escuelaProfesional);
    Optional<EscuelaProfesional> buscarPorId(Long id);
    Optional<EscuelaProfesionalProjection> obtenerDetallerPorId(Long id);
    List<EscuelaProfesionalProjection> listarTodosPorFacultad(Long facultadId);
    List<EscuelaProfesional> listarPorDepartamento(Long departamentoId);
    boolean existePorAbreviatura(String abreviatura);
    boolean existePorCodigo(Integer codigo);
}
