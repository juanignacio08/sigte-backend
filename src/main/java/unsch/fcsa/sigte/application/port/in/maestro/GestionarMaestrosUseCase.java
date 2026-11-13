package unsch.fcsa.sigte.application.port.in.maestro;

import unsch.fcsa.sigte.domain.model.maestro.Maestro;

import java.util.List;

public interface GestionarMaestrosUseCase {
    Maestro create(String tableCode, String itemCode, String denomination, Boolean isSystem);
    Maestro update(String code, String denomination, Boolean isSystem);
    List<Maestro> getTables(boolean isSystem);
    List<Maestro> getItemsByTableCode(String tableCode);
    List<Maestro> getItemsActivesByTableCode(String tableCode);
    Maestro activate(String code);
    Maestro deactivate(String code);
    void delete(String code);

    boolean existsItem(String tableCode, String code);
    Maestro getItem(String code);

}
