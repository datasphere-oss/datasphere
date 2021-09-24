package com.huahui.datasphere.mdm.core.service.impl;

import com.huahui.datasphere.mdm.core.context.AbstractModelChangeContext;
import com.huahui.datasphere.mdm.core.context.AbstractModelGetContext;
import com.huahui.datasphere.mdm.core.dto.AbstractModelResult;
import com.huahui.datasphere.mdm.core.type.model.ModelInstance;
import com.huahui.datasphere.mdm.core.type.model.source.AbstractModelSource;

/**
 * @author theseusyang on Dec 18, 2020
 */
public abstract class AbstractModelComponent {
    /**
     * Constructor.
     */
    protected AbstractModelComponent() {
        super();
    }
    /**
     * Sets info fields, common to all models.
     * @param target the target model
     * @param change the change source
     */
    protected void processInfoFields(AbstractModelSource<?> target, AbstractModelChangeContext change) {

        if (change.hasNameSet()) {
            target.withName(change.getName());
        }

        if (change.hasDescriptionSet()) {
            target.withDescription(change.getDescription());
        }

        if (change.hasDisplayNameSet()) {
            target.withDisplayName(change.getDisplayName());
        }
    }

    protected void processInfoFields(ModelInstance<?> i, AbstractModelGetContext get, AbstractModelResult result) {

        if (get.isModelInfo()) {
            result.setDescription(i.getDescription());
            result.setDisplayName(i.getDisplayName());
            result.setName(i.getName());
            result.setVersion(i.getVersion());
        }
    }
}
