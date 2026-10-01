package unsch.fcsa.sigte.infrastructure.cache;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.MasterEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro.SpringDataMasterRepository;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class MasterTableCache {

    private final SpringDataMasterRepository springDataMasterRepository;

    private Map<String, List<MasterEntity>> cacheByTable;
    private Map<String, MasterEntity> cacheByCode;

    @PostConstruct
    public void loadCache() {
        log.info("Cargando doble caché de Tabla Maestra...");
        List<MasterEntity> allItems = springDataMasterRepository.getAll();

        this.cacheByCode = allItems.stream()
                .collect(Collectors.toMap(
                        MasterEntity::getCode,
                        item -> item
                ));

        this.cacheByTable = allItems.stream()
                .filter(item -> !item.getItemCode().equals("000"))
                .sorted(Comparator.comparing(MasterEntity::getCode))
                .collect(Collectors.groupingBy(MasterEntity::getTableCode));

        log.info("Caché optimizado correctamente.");
    }

    public boolean existsItem(String tableCode, String code) {
        List<MasterEntity> tableItems = cacheByTable.get(tableCode);

        if (tableItems == null) {
            return false;
        }

        return tableItems.stream()
                .anyMatch(item -> item.getCode().equals(code));
    }

    public MasterEntity getItem(String code) {
        return cacheByCode.get(code);
    }

    public List<MasterEntity> getItemsByTableCode(String tableCode) {
        return cacheByTable.getOrDefault(tableCode, Collections.emptyList());
    }

    public List<MasterEntity> getItemsActivesByTableCode(String tableCode) {
        return cacheByTable.getOrDefault(tableCode, Collections.emptyList())
                .stream().filter(item -> item.getState() == 1)
                .toList();
    }

    public List<MasterEntity> getTables() {
        return cacheByCode.values().stream()
                .filter(item -> item.getItemCode().equals("000"))
                .sorted(Comparator.comparing(MasterEntity::getTableCode))
                .toList();
    }

    public List<MasterEntity> getTablesByIsSystem(Boolean isSystem) {
        return cacheByCode.values().stream()
                .filter(item -> item.getItemCode().equals("000") && item.getIsSystem().equals(isSystem))
                .sorted(Comparator.comparing(MasterEntity::getTableCode))
                .toList();
    }

    public void addItem(MasterEntity entity) {
        // 1. Registrar en la caché por código único
        this.cacheByCode.put(entity.getCode(), entity);

        // 2. Si no es una cabecera de tabla (itemCode != 000), agregarlo a su grupo
        if (!entity.getItemCode().equals("000")) {
            // Obtenemos la lista actual o creamos una nueva si es la primera vez que se usa esa tabla
            this.cacheByTable.computeIfAbsent(entity.getTableCode(), k -> new ArrayList<>())
                    .add(entity);

            // Opcional: Re-ordenar la lista para que el combo en el frontend aparezca ordenado
            this.cacheByTable.get(entity.getTableCode())
                    .sort(Comparator.comparing(MasterEntity::getCode));
        }
        log.info("Nuevo ítem añadido a la caché: [ {} ]", entity.getCode());
    }

    public void updateItem(MasterEntity entity) {
        // 1. Actualizamos la caché por código (O(1) - instantáneo)
        this.cacheByCode.put(entity.getCode(), entity);

        // 2. Actualizamos la lista de la tabla correspondiente
        if (!entity.getItemCode().equals("000")) {
            List<MasterEntity> items = this.cacheByTable.get(entity.getTableCode());
            if (items != null) {
                // Removemos la versión vieja y añadimos la nueva
                items.removeIf(item -> item.getCode().equals(entity.getCode()));
                items.add(entity);
                // Volvemos a ordenar para mantener la consistencia visual
                items.sort(Comparator.comparing(MasterEntity::getCode));
            }
        }
        log.info("Caché actualizada puntualmente para el código: {}", entity.getCode());
    }

    public void removeItem(String code) {
        MasterEntity entity = this.cacheByCode.remove(code);
        if (entity != null && !entity.getItemCode().equals("000")) {
            List<MasterEntity> items = this.cacheByTable.get(entity.getTableCode());
            if (items != null) {
                items.removeIf(item -> item.getCode().equals(code));
            }
        }
        log.info("Eliminando tabla del cache: {}", code);
    }

    public void reload() {
        loadCache();
    }


}
