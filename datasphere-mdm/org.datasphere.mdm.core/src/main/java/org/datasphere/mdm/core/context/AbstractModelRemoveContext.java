package org.datasphere.mdm.core.context;

import org.datasphere.mdm.system.context.AbstractCompositeRequestContext;

/**
 * @author Mikhail Mikhailov on Oct 8, 2020
 */
public abstract class AbstractModelRemoveContext extends AbstractCompositeRequestContext implements ModelRemoveContext {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 8768851190380167183L;
    /**
     * Storage ID to apply the updates to.
     */
    private final String storageId;
    /**
     * The model ID being update.
     */
    private final String modelId;
    /**
     * Constructor.
     */
    protected AbstractModelRemoveContext(AbstractModelRemoveContextBuilder<?> b) {
        super(b);
        this.storageId = b.storageId;
        this.modelId = b.modelId;
    }
    /**
     * @return the storageId
     */
    @Override
    public String getStorageId() {
        return storageId;
    }
    /**
     * @return the modelId
     */
    @Override
    public String getInstanceId() {
        return modelId;
    }
    /**
     * @author Mikhail Mikhailov on Oct 9, 2020
     */
    public abstract static class AbstractModelRemoveContextBuilder<X extends AbstractModelRemoveContextBuilder<X>>
        extends AbstractCompositeRequestContextBuilder<X> {
        /**
         * Storage ID to apply the updates to.
         */
        private String storageId;
        /**
         * The model ID being update.
         */
        private String modelId;
        /**
         * Constructor.
         */
        protected AbstractModelRemoveContextBuilder() {
            super();
        }
        /**
         * Sets storage ID.
         *
         * @param storageId the ID
         * @return self
         */
        public X storageId(String storageId) {
            this.storageId = storageId;
            return self();
        }
        /**
         * Sets model ID.
         *
         * @param modelId the ID
         * @return self
         */
        public X modelId(String modelId) {
            this.modelId = modelId;
            return self();
        }
    }
}
