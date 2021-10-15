package org.datasphere.mdm.core.context;

import org.datasphere.mdm.core.type.model.ModelSource;
import org.datasphere.mdm.system.context.AbstractCompositeRequestContext;

/**
 * @author Mikhail Mikhailov on Oct 8, 2020
 */
public abstract class AbstractModelSourceContext<T extends ModelSource>
    extends AbstractCompositeRequestContext
    implements ModelSourceContext<T> {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 3678657448757736134L;
    /**
     * Storage ID to apply the updates to.
     */
    private final String storageId;
    /**
     * The model ID being update.
     */
    private final String modelId;
    /**
     * The model source.
     */
    private final T source;
    /**
     * Constructor.
     */
    protected AbstractModelSourceContext(AbstractModelSourceContextBuilder<T, ?> b) {
        super(b);
        this.storageId = b.storageId;
        this.modelId = b.modelId;
        this.source = b.source;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public T getSource() {
        return source;
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
    public abstract static class AbstractModelSourceContextBuilder<T extends ModelSource, X extends AbstractModelSourceContextBuilder<T, X>>
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
         * The model source.
         */
        private T source;
        /**
         * Constructor.
         */
        protected AbstractModelSourceContextBuilder() {
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
        /**
         * Sets model source.
         *
         * @param source the model source
         * @return self
         */
        public X source(T source) {
            this.source = source;
            return self();
        }
    }
}
