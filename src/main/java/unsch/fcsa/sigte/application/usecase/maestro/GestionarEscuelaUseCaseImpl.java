package unsch.fcsa.sigte.application.usecase.maestro;

import unsch.fcsa.sigte.domain.model.maestro.EscuelaProfesional;
import unsch.fcsa.sigte.domain.port.in.maestro.GestionarEscuelaUseCase;
import unsch.fcsa.sigte.domain.port.out.maestro.DepartamentoRepositoryPort;
import unsch.fcsa.sigte.domain.port.out.maestro.EscuelaProfesionalRepositoryPort;

import java.util.List;

public class GestionarEscuelaUseCaseImpl implements GestionarEscuelaUseCase {

    private final EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort;
    private final DepartamentoRepositoryPort departamentoRepositoryPort;

    public GestionarEscuelaUseCaseImpl(
            EscuelaProfesionalRepositoryPort escuelaProfesionalRepositoryPort,
            DepartamentoRepositoryPort departamentoRepositoryPort
    ) {
        this.escuelaProfesionalRepositoryPort = escuelaProfesionalRepositoryPort;
        this.departamentoRepositoryPort = departamentoRepositoryPort;
    }

    @Override
    public EscuelaProfesional crearEscuela(Long departamentoId, Integer codigo, String abreviatura, String denominacion, String plantillaMemo, String plantillaRes) {
        if (escuelaProfesionalRepositoryPort.existePorCodigo(codigo)) {
            throw new IllegalArgumentException("El codigo " + codigo + " ya existe en el sistema");
        }

        if (escuelaProfesionalRepositoryPort.existePorAbreviatura(abreviatura)) {
            throw new IllegalArgumentException("Ya existe una escuela con la abreviatura " + abreviatura);
        }

        if (!departamentoRepositoryPort.existePorId(departamentoId)) {
            throw new IllegalArgumentException("El departamento " + departamentoId + " no existe");
        }

        EscuelaProfesional escuelaProfesional = new EscuelaProfesional(
                departamentoId, codigo, abreviatura, denominacion, plantillaMemo, plantillaRes
        );

        return escuelaProfesionalRepositoryPort.guardar(escuelaProfesional);
    }

    @Override
    public EscuelaProfesional modificarEscuela(Long id, Long departamentoId, Integer codigo, String abreviatura, String denominacion, String plantillaMemo, String plantillaRes) {
        EscuelaProfesional existente = obtenerPorId(id);

        if (!existente.getAbreviatura().equalsIgnoreCase(abreviatura) &&
                escuelaProfesionalRepositoryPort.existePorAbreviatura(abreviatura)) {
            throw new IllegalArgumentException("La abreviatura " + abreviatura + " ya está registrada por otra escuela.");
        }

        if (!existente.getCodigo().equals(codigo) &&
                escuelaProfesionalRepositoryPort.existePorCodigo(codigo)) {
            throw new IllegalArgumentException("El codigo " + codigo + " ya está registrado por otra escuela.");
        }

        if (!existente.getDepartamentoAcademicoId().equals(departamentoId) &&
                !departamentoRepositoryPort.existePorId(departamentoId)) {
            throw new IllegalArgumentException("No existe un departamento academico con el id " + departamentoId);
        }

        existente.actualizarDatos(departamentoId, codigo, abreviatura, denominacion, plantillaMemo, plantillaRes);

        return escuelaProfesionalRepositoryPort.guardar(existente);
    }

    @Override
    public EscuelaProfesional obtenerPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id de escuela no puede ser null o negativo.");
        }

        return escuelaProfesionalRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la escuela con ID: " + id));
    }

    @Override
    public List<EscuelaProfesional> listarTodos() {
        return escuelaProfesionalRepositoryPort.listarTodos();
    }

    @Override
    public void eliminarEscuela(Long id) {
        EscuelaProfesional escuela = obtenerPorId(id);
        escuela.marcarComoEliminado();
        escuelaProfesionalRepositoryPort.guardar(escuela);
    }
}
