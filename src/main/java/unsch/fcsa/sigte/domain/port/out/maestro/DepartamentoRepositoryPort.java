package unsch.fcsa.sigte.domain.port.out.maestro;

import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;

import java.util.List;
import java.util.Optional;

public interface DepartamentoRepositoryPort {

    DepartamentoAcademico guardar(DepartamentoAcademico departamento);

    Optional<DepartamentoAcademico> buscarPorId(Long id);

    List<DepartamentoAcademico> listarTodos();

    boolean existePorAbreviatura(String abreviatura);

    boolean existePorId(Long id);
}
