package unsch.fcsa.sigte.application.usecase.maestro;

import unsch.fcsa.sigte.domain.exception.DuplicateKeyException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.maestro.DepartamentoAcademico;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarDepartamentoUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.DepartamentoRepositoryPort;

import java.util.List;

public class GestionarDepartamentoUseCaseImpl implements GestionarDepartamentoUseCase {

    private final DepartamentoRepositoryPort departamentoRepositoryPort;

    // Inyección de dependencias por constructor
    public GestionarDepartamentoUseCaseImpl(DepartamentoRepositoryPort departamentoRepositoryPort) {
        this.departamentoRepositoryPort = departamentoRepositoryPort;
    }

    @Override
    public DepartamentoAcademico crearDepartamento(Long facultadId, String abreviatura, String denominacion) {
        if (departamentoRepositoryPort.existePorAbreviatura(abreviatura)) {
            throw new DuplicateKeyException("Departamento", "abreviatura", abreviatura);
        }

        DepartamentoAcademico nuevoDepartamento = new DepartamentoAcademico(facultadId, abreviatura, denominacion);

        return departamentoRepositoryPort.guardar(nuevoDepartamento);
    }

    @Override
    public DepartamentoAcademico obtenerPorId(Long id) {
        return departamentoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Departamento", "id", id));
    }

    @Override
    public List<DepartamentoAcademico> listarTodos() {
        return departamentoRepositoryPort.listarTodos();
    }

    @Override
    public DepartamentoAcademico actualizarDepartamento(Long id, String abreviatura, String denominacion) {
        DepartamentoAcademico existente = obtenerPorId(id);

        if (!existente.getAbreviatura().equalsIgnoreCase(abreviatura) &&
                departamentoRepositoryPort.existePorAbreviatura(abreviatura)) {
            throw new DuplicateKeyException("Departamento", "abreviatura", abreviatura);
        }

        existente.actualizarDatos(abreviatura, denominacion);
        return departamentoRepositoryPort.guardar(existente);
    }

    @Override
    public void eliminarDepartamento(Long id) {
        DepartamentoAcademico departamento = obtenerPorId(id);

        // Aplicamos el borrado lógico desde la entidad de dominio
        departamento.marcarComoEliminado();

        departamentoRepositoryPort.guardar(departamento);
    }
}
