package unsch.fcsa.sigte.application.port.out.maestro;

import unsch.fcsa.sigte.domain.model.maestro.Facultad;

import java.util.Optional;

public interface FacultadRepositoryPort {
    Optional<Facultad> buscarPorId(Long id);
    boolean existePorId(Long id);
}
