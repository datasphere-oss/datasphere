package org.datasphere.mdm.core.context;

import org.datasphere.mdm.system.context.AbstractCompositeRequestContext;

/**
 * @author Mikhail Mikhailov on Oct 8, 2020
 */
public abstract class AbstractModelChangeContext extends AbstractCompositeRequestContext implements ModelChangeContext {
    /**
     * Name set.
     */
    private static final int NAME_SET_MARK = 1 << 0;
    /**
     * Display name set.
     */
    private static final int DISPLAY_NAME_SET_MARK = 1 << 1;
    /**
     * Description set.
     */
    private static final int DESCRIPTION_SET_MARK = 1 << 2;
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = -57473327638268493L;
    /**
     * Storage ID to apply the updates to.
     */
    private final String storageId;
    /**
     * The model ID being update.
     */
    private final String instanceId;
    /**
     * Drop existing model and create a new one (no merge).
     */
    private final ModelChangeType upsertType;
    /**
     * The model name.
     */
    private final String name;
    /**
     * The model display name.
     */
    private final String displayName;
    /**
     * The model description.
     */
    private final String description;
    /**
     * Wait until model is aplied and then return.
     */
    private final boolean waitForFinish;
    /**
     * Force upsert.
     */
    private final boolean force;
    /**
     * Unrefresh.
     */
    private final boolean postponeRefresh;
    /**
     * The version to apply (current + 1 otherwise).
     */
    private final Integer version;
    /**
     * Info fields state.
     */
    private final int state;
    /**
     * Constructor.
     */
    protected AbstractModelChangeContext(AbstractModelChangeContextBuilder<?> b) {
        super(b);
        this.storageId = b.storageId;
        this.instanceId = b.instanceId;
        this.upsertType = b.upsertType;
        this.name = b.name;
        this.displayName = b.displayName;
        this.description = b.description;
        this.state = b.state;
        this.waitForFinish = b.waitForFinish;
        this.force = b.force;
        this.postponeRefresh = b.postponeRefresh;
        this.version = b.version;
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
     * @return flag responsible for cleaning current DB and cache state
     */
    public ModelChangeType getUpsertType() {
        return upsertType;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * Returns true, if name field has been set.
     * @return true, if name field has been set.
     */
    public boolean hasNameSet() {
        return (state & NAME_SET_MARK) != 0;
    }
    /**
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }
    /**
     * Returns true, if display name field has been set.
     * @return true, if display name field has been set.
     */
    public boolean hasDisplayNameSet() {
        return (state & DISPLAY_NAME_SET_MARK) != 0;
    }
    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Returns true, if description field has been set.
     * @return true, if description field has been set.
     */
    public boolean hasDescriptionSet() {
        return (state & DESCRIPTION_SET_MARK) != 0;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean waitForFinish() {
        return waitForFinish;
    }
    /**
     * @return the force
     */
    @Override
    public boolean force() {
        return force;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean postponeRefresh() {
        return postponeRefresh;
    }
    /**
     * @return the version
     */
    @Override
    public Integer getVersion() {
        return version;
    }
    /**
     * @author Mikhail Mikhailov on Oct 9, 2020
     */
    public abstract static class AbstractModelChangeContextBuilder<X extends AbstractModelChangeContextBuilder<X>> extends AbstractCompositeRequestContextBuilder<X> {
        /**
         * Storage ID to apply the updates to.
         */
        private String storageId;
        /**
         * The model ID being update.
         */
        private String instanceId;
        /**
         * The model name.
         */
        private String name;
        /**
         * The model display name.
         */
        private String displayName;
        /**
         * The model description.
         */
        private String description;
        /**
         * Info fields state.
         */
        private int state = 0;
        /**
         * Wait until model is aplied and then return.
         */
        private boolean waitForFinish;
        /**
         * Upsert.
         */
        private boolean force;
        /**
         * Unrefresh.
         */
        private boolean postponeRefresh;
        /**
         * The version to apply (current + 1 otherwise).
         */
        private Integer version;
        /**
         * Update mode.
         */
        private ModelChangeType upsertType = ModelChangeType.PARTIAL;
        /**
         * Constructor.
         */
        protected AbstractModelChangeContextBuilder() {
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
         * Sets model description.
         *
         * @param description the model description
         * @return self
         */
        public X description(String description) {
            this.description = description;
            this.state |= DESCRIPTION_SET_MARK;
            return self();
        }
        /**
         * Sets model display name.
         *
         * @param displayName the display name
         * @return self
         */
        public X displayName(String displayName) {
            this.displayName = displayName;
            this.state |= DISPLAY_NAME_SET_MARK;
            return self();
        }
        /**
         * Sets model name.
         *
         * @param name the name
         * @return self
         */
        public X name(String name) {
            this.name = name;
            this.state |= NAME_SET_MARK;
            return self();
        }
        /**
         * Sets upsert type.
         *
         * @param upsertType the type
         * @return self
         */
        public X upsertType(ModelChangeType upsertType) {
            this.upsertType = upsertType;
            return self();
        }
        /**
         * Wait until model is aplied and then return.
         *
         * @param upsertType the type
         * @return self
         */
        public X waitForFinish(boolean waitForFinish) {
            this.waitForFinish = waitForFinish;
            return self();
        }
        /**
         * Force upsert, even if supplied version is older then current + 1.
         *
         * @param force the force state
         * @return self
         */
        public X force(boolean force) {
            this.force = force;
            return self();
        }
        /**
         * Give unrefresh hint.
         *
         * @param postponeRefresh the unrefresh state
         * @return self
         */
        public X postponeRefresh(boolean postponeRefresh) {
            this.postponeRefresh = postponeRefresh;
            return self();
        }
        /**
         * Sets specific version to apply (otherwise current + 1).
         *
         * @param version the version to apply
         * @return self
         */
        public X version(Integer version) {
            this.version = version;
            return self();
        }
    }
}
