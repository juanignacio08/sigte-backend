package unsch.fcsa.sigte.application.port.in.maestro;

import unsch.fcsa.sigte.domain.model.maestro.Facultad;

public interface GestionarFacultadUseCase {
    Facultad buscarPorId(Long id);
}
