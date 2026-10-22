package unsch.fcsa.sigte.application.port.out.tramites;

import unsch.fcsa.sigte.domain.model.tramites.ExpedienteArchivo;

import java.util.List;

public interface ExpedienteArchivoRepositoryPort {
    void guardarTodos(List<ExpedienteArchivo> archivos);
    List<ExpedienteArchivo> obtenerPorExpediente(Long expedienteId);
}
