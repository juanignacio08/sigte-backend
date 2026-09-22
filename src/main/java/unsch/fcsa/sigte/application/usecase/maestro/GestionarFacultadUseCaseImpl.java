package unsch.fcsa.sigte.application.usecase.maestro;

import unsch.fcsa.sigte.application.port.in.maestro.GestionarFacultadUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.FacultadRepositoryPort;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.maestro.Facultad;

public class GestionarFacultadUseCaseImpl implements GestionarFacultadUseCase {

    private final FacultadRepositoryPort facultadRepositoryPort;

    public GestionarFacultadUseCaseImpl(FacultadRepositoryPort facultadRepositoryPort) {
        this.facultadRepositoryPort = facultadRepositoryPort;
    }

    @Override
    public Facultad buscarPorId(Long id) {
        return facultadRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facultad", "id", id));
    }
}
