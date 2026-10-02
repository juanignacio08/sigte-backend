package unsch.fcsa.sigte.infrastructure.adapter.output.persistence.adapter.maestro;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import unsch.fcsa.sigte.application.port.out.maestro.MaestroRepositoryPort;
import unsch.fcsa.sigte.domain.model.maestro.Maestro;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.entity.maestro.MasterEntity;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.mapper.maestro.MasterMapperOut;
import unsch.fcsa.sigte.infrastructure.adapter.output.persistence.repository.maestro.SpringDataMasterRepository;
import unsch.fcsa.sigte.infrastructure.cache.MasterTableCache;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MaestroPersistenceAdapter implements MaestroRepositoryPort {

    private final SpringDataMasterRepository springDataRepository;
    private final MasterTableCache masterTableCache;
    private final MasterMapperOut mapper;

    @Override
    @Transactional
    public Maestro register(Maestro maestro) {
        MasterEntity entity = mapper.toEntity(maestro);
        MasterEntity saved = springDataRepository.save(entity);

        // Sincronizar con el Caché Manual en RAM ÚNICAMENTE si la transacción hace COMMIT
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    masterTableCache.addItem(saved);
                }
            });
        } else {
            // Si por alguna razón la llamada no fuera transaccional, actualiza directamente
            masterTableCache.addItem(saved);
        }

        return mapper.toDomain(saved);
    }

    @Override
    @Transactional
    public Maestro update(Maestro maestro) {
        MasterEntity entity = mapper.toEntity(maestro);
        MasterEntity saved = springDataRepository.save(entity);

        // Sincronizar con el Caché Manual en RAM ÚNICAMENTE si la transacción hace COMMIT
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    masterTableCache.updateItem(saved);
                }
            });
        } else {
            // Si por alguna razón la llamada no fuera transaccional, actualiza directamente
            masterTableCache.updateItem(saved);
        }

        return mapper.toDomain(saved);
    }

    @Override
    public List<Maestro> getTables(boolean isSystem) {
        List<MasterEntity> list = masterTableCache.getTablesByIsSystem(isSystem);
        return list.stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Maestro> getItemsByTable(String tableCode) {
        List<MasterEntity> list = masterTableCache.getItemsByTableCode(tableCode);
        return list.stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Maestro> getItemsActivesByTable(String tableCode) {
        List<MasterEntity> list = masterTableCache.getItemsActivesByTableCode(tableCode);
        return list.stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void delete(Maestro maestro) {
        MasterEntity entity = mapper.toEntity(maestro);
        MasterEntity saved = springDataRepository.save(entity);

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    masterTableCache.removeItem(saved.getCode());
                }
            });
        } else {
            masterTableCache.removeItem(saved.getCode());
        }
    }

    @Override
    public boolean existsByCode(String code) {
        return masterTableCache.existsItem(code);
    }

    @Override
    public Maestro getItemByCode(String code) {
        return mapper.toDomain(masterTableCache.getItem(code));
    }

    @Override
    public Optional<Maestro> getByCode(String code) {
        return springDataRepository.findByCodeAndIsDeletedFalse(code).map(mapper::toDomain);
    }

}
