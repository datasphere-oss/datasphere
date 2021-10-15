package org.datasphere.mdm.core.service.impl.instance;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.po.model.StoragePO;
import org.datasphere.mdm.core.type.model.StorageElement;
import org.datasphere.mdm.core.type.model.StorageInstance;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.datasphere.mdm.system.exception.PlatformFailureException;

/**
 * @author Mikhail Mikhailov on Oct 5, 2020
 */
public class StorageInstanceImpl implements StorageInstance {
    /**
     * The storages.
     */
    private final Map<String, StorageElement> storages;
    /**
     * Constructor.
     */
    public StorageInstanceImpl(List<StoragePO> pos) {
        super();

        if (CollectionUtils.isEmpty(pos)) {
            throw new PlatformFailureException(
                    "Storage model empty. Platform not functional.",
                    CoreExceptionIds.EX_META_STORAGE_MODEL_EMPTY);
        }

        this.storages = pos.stream()
                .map(po -> {

                    StorageImpl s = new StorageImpl();

                    s.setStorageId(po.getId());
                    s.setDescription(po.getDescription());
                    s.setCreatedBy(po.getCreatedBy());
                    s.setUpdatedBy(po.getUpdatedBy());
                    s.setCreateDate(po.getCreateDate() == null ? null : po.getCreateDate().toInstant());
                    s.setUpdateDate(po.getUpdateDate() == null ? null : po.getUpdateDate().toInstant());

                    return s;
                })
                .collect(Collectors.toMap(StorageImpl::getStorageId, Function.identity()));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<StorageElement> getActive() {
        return storages.values();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public StorageElement getStorageForCurrentUser() {
        return storages.get(SecurityUtils.getCurrentUserStorageId());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public StorageElement getStorageById(String storageId) {
        return storages.get(storageId);
    }
}
