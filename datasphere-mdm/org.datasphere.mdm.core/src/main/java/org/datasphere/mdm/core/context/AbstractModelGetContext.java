package org.datasphere.mdm.core.context;

import org.datasphere.mdm.system.context.AbstractCompositeRequestContext;

/**
 * @author Mikhail Mikhailov on Oct 8, 2020
 */
public abstract class AbstractModelGetContext extends AbstractCompositeRequestContext implements ModelGetContext {
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
    private final String instanceId;
    /**
     * Return common model info fields.
     */
    private final boolean modelInfo;
    /**
     * Constructor.
     */
    protected AbstractModelGetContext(AbstractModelGetContextBuilder<?> b) {
        super(b);
        this.storageId = b.storageId;
        this.instanceId = b.instanceId;
        this.modelInfo = b.modelInfo;
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
        return instanceId;
    }
    /**
     * @return modelInfo flag
     */
    public boolean isModelInfo() {
        return modelInfo;
    }
    /**
     * @author Mikhail Mikhailov on Oct 9, 2020
     */
    public abstract static class AbstractModelGetContextBuilder<X extends AbstractModelGetContextBuilder<X>> extends AbstractCompositeRequestContextBuilder<X> {
        /**
         * Storage ID to apply the updates to.
         */
        private String storageId;
        /**
         * The model ID being update.
         */
        private String instanceId;
        /**
         * Return common model info fields.
         */
        private boolean modelInfo;
        /**
         * Constructor.
         */
        protected AbstractModelGetContextBuilder() {
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
         * @param instanceId the ID
         * @return self
         */
        public X instanceId(String instanceId) {
            this.instanceId = instanceId;
            return self();
        }
        /**
         * Requests common model info.
         *
         * @param modelInfo state
         * @return self
         */
        public X modelInfo(boolean modelInfo) {
            this.modelInfo = modelInfo;
            return self();
        }
    }
}
