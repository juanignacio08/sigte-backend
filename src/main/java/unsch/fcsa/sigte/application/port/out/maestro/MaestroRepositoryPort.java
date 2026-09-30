package unsch.fcsa.sigte.application.port.out.maestro;

import unsch.fcsa.sigte.domain.model.maestro.Maestro;

import java.util.List;
import java.util.Optional;

public interface MaestroRepositoryPort {
    Maestro register(Maestro maestro);
    Maestro update(Maestro maestro);
    List<Maestro> getTables(boolean isSystem);
    List<Maestro> getItemsByTable(String tableCode);
    List<Maestro> getItemsActivesByTable(String tableCode);
    void delete(Maestro maestro);
    boolean existsByCode(String code);
    Optional<Maestro> getByCode(String code);
}
