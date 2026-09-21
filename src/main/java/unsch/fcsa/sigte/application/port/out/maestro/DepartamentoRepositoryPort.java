package unsch.fcsa.sigte.application.port.out.maestro;

import unsch.fcsa.sigte.application.port.out.maestro.projections.DepartamentoConTotalEscuelasView;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;

import java.util.List;
import java.util.Optional;

public interface DepartamentoRepositoryPort {

    DepartamentoAcademico guardar(DepartamentoAcademico departamento);

    Optional<DepartamentoAcademico> buscarPorId(Long id);

    List<DepartamentoConTotalEscuelasView> listarTodosPorFacultad(Long facultadId);

    boolean existePorAbreviatura(String abreviatura);

    boolean existePorId(Long id);
}
