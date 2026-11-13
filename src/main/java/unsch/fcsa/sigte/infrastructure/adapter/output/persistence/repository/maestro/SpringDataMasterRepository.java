package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.MasterEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataMasterRepository extends JpaRepository<MasterEntity, Long> {

    boolean existsByCodeAndIsDeletedFalse(String code);

    Optional<MasterEntity> findByCodeAndIsDeletedFalse(String code);

    List<MasterEntity> findByItemCodeAndIsDeletedFalse(String itemCode);
    default List<MasterEntity> getTables() {
        return findByItemCodeAndIsDeletedFalse("000");
    }

    List<MasterEntity> findByItemCodeAndIsSystemAndIsDeletedFalse(String itemCode, boolean isSystem);
    default List<MasterEntity> getTablesByIsSystem(boolean isSystem) {
        return findByItemCodeAndIsSystemAndIsDeletedFalse("000", isSystem);
    }

    List<MasterEntity> findByTableCodeAndItemCodeNotAndIsDeletedFalse(String tableCode, String itemCode);
    default List<MasterEntity> getAllItemsByTable(String tableCode) {
        return findByTableCodeAndItemCodeNotAndIsDeletedFalse(tableCode, "000");
    }

    List<MasterEntity> findByTableCodeAndItemCodeNotAndStateAndIsDeletedFalse(String tableCode, String itemCode, int state);
    default List<MasterEntity> getAllActivesItemsByTable(String tableCode) {
        return findByTableCodeAndItemCodeNotAndStateAndIsDeletedFalse(tableCode, "000", 1);
    }

    List<MasterEntity> findByIsDeletedFalseOrderByCode();
    default List<MasterEntity> getAll() {
        return findByIsDeletedFalseOrderByCode();
    }


}
