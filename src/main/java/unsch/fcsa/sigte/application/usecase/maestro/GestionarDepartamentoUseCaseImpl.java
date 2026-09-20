package unsch.fcsa.sigte.application.usecase.maestro;

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
        // Validar regla de negocio de duplicidad
        if (departamentoRepositoryPort.existePorAbreviatura(abreviatura)) {
            throw new IllegalArgumentException("Ya existe un departamento con la abreviatura: " + abreviatura);
        }

        // Crear la entidad pura de dominio (aquí se aplican sus validaciones internas)
        DepartamentoAcademico nuevoDepartamento = new DepartamentoAcademico(facultadId, abreviatura, denominacion);

        // Guardar mediante el puerto de salida
        return departamentoRepositoryPort.guardar(nuevoDepartamento);
    }

    @Override
    public DepartamentoAcademico obtenerPorId(Long id) {
        return departamentoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el departamento académico con ID: " + id));
    }

    @Override
    public List<DepartamentoAcademico> listarTodos() {
        return departamentoRepositoryPort.listarTodos();
    }

    @Override
    public DepartamentoAcademico actualizarDepartamento(Long id, String abreviatura, String denominacion) {
        DepartamentoAcademico existente = obtenerPorId(id);

        // Si la abreviatura cambia, verificar que la nueva no esté en uso por otro departamento
        if (!existente.getAbreviatura().equalsIgnoreCase(abreviatura) &&
                departamentoRepositoryPort.existePorAbreviatura(abreviatura)) {
            throw new IllegalArgumentException("La abreviatura " + abreviatura + " ya está registrada por otro departamento.");
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
