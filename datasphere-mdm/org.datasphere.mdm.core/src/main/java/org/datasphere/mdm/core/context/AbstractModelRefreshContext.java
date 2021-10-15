package org.datasphere.mdm.core.context;

import org.datasphere.mdm.system.context.AbstractCompositeRequestContext;

/**
 * @author Mikhail Mikhailov on Oct 8, 2020
 */
public abstract class AbstractModelRefreshContext extends AbstractCompositeRequestContext implements ModelRefreshContext, PreviousModelStateContext {
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
     * The model type ID.
     */
    private final String typeId;
    /**
     * Local refresh.
     */
    private final boolean local;
    /**
     * Constructor.
     */
    protected AbstractModelRefreshContext(AbstractModelRefreshContextBuilder<?> b) {
        super(b);
        this.storageId = b.storageId;
        this.instanceId = b.instanceId;
        this.typeId = b.typeId;
        this.local = b.local;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getStorageId() {
        return storageId;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getInstanceId() {
        return instanceId;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeId() {
        return typeId;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLocal() {
        return local;
    }
    /**
     * @author Mikhail Mikhailov on Oct 9, 2020
     */
    public abstract static class AbstractModelRefreshContextBuilder<X extends AbstractModelRefreshContextBuilder<X>>
        extends AbstractCompositeRequestContextBuilder<X> {
        /**
         * Storage ID to apply the updates to.
         */
        private String storageId;
        /**
         * The model ID being update.
         */
        private String instanceId;
        /**
         * The model type ID.
         */
        private String typeId;
        /**
         * Locally triggered refresh mark.
         */
        private boolean local;
        /**
         * Constructor.
         */
        protected AbstractModelRefreshContextBuilder() {
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
         * Sets model type ID.
         *
         * @param typeId the ID
         * @return self
         */
        public X typeId(String typeId) {
            this.typeId = typeId;
            return self();
        }
        /**
         * Sets local refresh mark.
         *
         * @param local the mark
         * @return self
         */
        public X local(boolean local) {
            this.local = local;
            return self();
        }
    }
}
