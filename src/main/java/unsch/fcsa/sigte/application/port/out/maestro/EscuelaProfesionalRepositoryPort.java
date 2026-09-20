package unsch.fcsa.sigte.application.port.out.maestro;

import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;

import java.util.List;
import java.util.Optional;

public interface EscuelaProfesionalRepositoryPort {
    EscuelaProfesional guardar(EscuelaProfesional escuelaProfesional);
    Optional<EscuelaProfesional> buscarPorId(Long id);
    Optional<EscuelaProfesional> buscarPorCodigo(Integer codigo);
    List<EscuelaProfesional> listarTodos();
    boolean existePorAbreviatura(String abreviatura);
    boolean existePorCodigo(Integer codigo);
}
