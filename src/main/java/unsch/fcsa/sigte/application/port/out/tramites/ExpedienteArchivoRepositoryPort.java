package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;

import java.util.List;

public interface ExpedienteArchivoRepositoryPort {
    ExpedienteArchivo guardar(ExpedienteArchivo domain);
    void eliminar(Long id);
    void guardarTodos(List<ExpedienteArchivo> archivos);
    List<ExpedienteArchivo> obtenerPorExpediente(Long expedienteId);
    ExpedienteArchivo obtenerPorId(Long id);
}
