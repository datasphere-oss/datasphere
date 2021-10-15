package org.datasphere.mdm.core.service.impl;

import java.util.Objects;

import org.datasphere.mdm.core.context.AbstractModelChangeContext;
import org.datasphere.mdm.core.context.AbstractModelGetContext;
import org.datasphere.mdm.core.dto.AbstractModelResult;
import org.datasphere.mdm.core.type.model.ModelInstance;
import org.datasphere.mdm.core.type.model.source.AbstractModelSource;

/**
 * @author Mikhail Mikhailov on Dec 18, 2020
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
     * @param source the source model
     * @param change the change source
     */
    protected void processInfoFields(AbstractModelSource<?> target, AbstractModelSource<?> source, AbstractModelChangeContext change) {

        if (change.hasNameSet()) {
            target.withName(change.getName());
        } else if (Objects.nonNull(source)) {
            target.withName(source.getName());
        }

        if (change.hasDescriptionSet()) {
            target.withDescription(change.getDescription());
        } else if (Objects.nonNull(source)) {
            target.withDescription(source.getDescription());
        }

        if (change.hasDisplayNameSet()) {
            target.withDisplayName(change.getDisplayName());
        } else if (Objects.nonNull(source)) {
            target.withDisplayName(source.getDisplayName());
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
