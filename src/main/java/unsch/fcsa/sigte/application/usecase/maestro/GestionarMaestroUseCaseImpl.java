package unsch.fcsa.sigte.application.usecase.maestro;

import lombok.extern.slf4j.Slf4j;
import unsch.fcsa.sigte.application.port.in.maestro.GestionarMaestrosUseCase;
import unsch.fcsa.sigte.application.port.out.maestro.MaestroRepositoryPort;
import unsch.fcsa.sigte.domain.exception.DuplicateKeyException;
import unsch.fcsa.sigte.domain.exception.ResourceNotFoundException;
import unsch.fcsa.sigte.domain.model.maestro.Maestro;

import java.util.List;

@Slf4j
public class GestionarMaestroUseCaseImpl implements GestionarMaestrosUseCase {

    private final MaestroRepositoryPort maestroRepositoryPort;

    public GestionarMaestroUseCaseImpl(MaestroRepositoryPort maestroRepositoryPort) {
        this.maestroRepositoryPort = maestroRepositoryPort;
    }

    @Override
    public Maestro create(String tableCode, String itemCode, String denomination, Boolean isSystem) {
        log.info("Registrando en tabla maestra: {}", tableCode);

        if (maestroRepositoryPort.existsByCode(tableCode.concat(itemCode))) {
            throw new DuplicateKeyException("Maestro", "codigoTabla/codigoItem",
                    tableCode + "-" + itemCode);
        }

        Maestro newMaster = new Maestro(
                tableCode,
                itemCode,
                denomination,
                isSystem
        );

        return maestroRepositoryPort.register(newMaster);
    }

    @Override
    public Maestro update(String code, String denomination, Boolean isSystem) {
        log.info("Actualizando tabla maestra: {}", code);

        Maestro maestro = getByCode(code);

        maestro.actualizarDatos(denomination, isSystem);

        return maestroRepositoryPort.update(maestro);
    }

    @Override
    public List<Maestro> getTables(boolean isSystem) {
        log.info("Listando tablas maestros [isSystem={}]", isSystem);
        return maestroRepositoryPort.getTables(isSystem);
    }

    @Override
    public List<Maestro> getItemsByTableCode(String tableCode) {
        log.info("Listando items de una tabla [tableCode={}]", tableCode);
        return maestroRepositoryPort.getItemsByTable(tableCode);
    }

    @Override
    public List<Maestro> getItemsActivesByTableCode(String tableCode) {
        log.info("Listando items activos de una tabla [tableCode={}]", tableCode);
        return maestroRepositoryPort.getItemsActivesByTable(tableCode);
    }

    @Override
    public Maestro activate(String code) {
        log.info("Activando tabla maestro: {}", code);
        Maestro maestro = getByCode(code);
        if (maestro.getEstado().equals(1)) {
            return maestro;
        }

        maestro.activar();
        return maestroRepositoryPort.update(maestro);
    }

    @Override
    public Maestro deactivate(String code) {
        log.info("Deactivando tabla maestro: {}", code);
        Maestro maestro = getByCode(code);
        if (maestro.getEstado().equals(0)) {
            return maestro;
        }
        maestro.desactivar();
        return maestroRepositoryPort.update(maestro);
    }

    @Override
    public void delete(String code) {
        log.info("Eliminando tabla maestro: {}", code);
        Maestro maestro = getByCode(code);
        maestro.marcarComoEliminado();
        maestroRepositoryPort.delete(maestro);
    }

    @Override
    public boolean existsItem(String tableCode, String code) {
        return false;
    }

    @Override
    public Maestro getItem(String code) {
        return null;
    }

    private Maestro getByCode(String code) {
        log.info("Buscando el maestro: {}", code);

        return maestroRepositoryPort.getByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Maestro", "código", code));
    }
}
