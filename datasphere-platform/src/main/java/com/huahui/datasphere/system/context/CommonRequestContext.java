

package com.huahui.datasphere.system.context;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.huahui.datasphere.system.util.IdUtils;

/**
 * @author theseusyang
 *         Base transient storage capable context class.
 * TODO: Remove serializable interface!
 */
public abstract class CommonRequestContext implements Serializable, BooleanFlagsContext, StorageCapableContext, ThrowableCauseAwareContext, InputContainer {
    /**
     * Default SVUID.
     */
    private static final long serialVersionUID = 5163431222466721757L;
    /**
     * The boolean field id atomic counter.
     * It is used to distribute bit set index poisitions without clashes.
     * See children for examples.
     */
    public static final AtomicInteger FLAG_ID_PROVIDER = new AtomicInteger(0);
    /**
     * The dependencies.
     */
    public static final StorageId SID_DEPENDENT_CONTEXTS = new StorageId("DEPENDENT_CONTEXTS");
    /**
     * Simple transient storage.
     * This map is intentionally made to work with object pointers.
     * See children for examples.
     */
    protected final transient Map<StorageId, Object> systemStorage = new IdentityHashMap<>();
    /**
     * Even simplier storage for arbitrary objects,
     * which are not stationary part of the context.
     */
    protected final transient Map<String, Object> userStorage = new HashMap<>();
    /**
     * Various boolean flags.
     */
    protected final BitSet flags = new BitSet();
    /**
     * Operation id, special attribute for grouping, tracing, etc. actions.
     */
    protected String operationId;
    /**
     * Constructor.
     */
    protected CommonRequestContext(CommonRequestContextBuilder<?> b) {
        super();
        this.operationId = b.operationId;
        if (Objects.nonNull(b.parent)) {

            List<CommonRequestContext> dependencies = b.parent.getFromStorage(SID_DEPENDENT_CONTEXTS);
            if (Objects.isNull(dependencies)) {
                dependencies = new ArrayList<>();
                b.parent.putToStorage(SID_DEPENDENT_CONTEXTS, dependencies);
            }

            dependencies.add(this);
        }
    }
    /**
     * Sets a known flag true.
     * @param flag the flag to set
     */
    @Override
    public void setFlag(int flag) {
        flags.set(flag);
    }
    /**
     * Sets a known flag false.
     * @param flag the flag to clear
     */
    @Override
    public void clearFlag(int flag) {
        flags.clear(flag);
    }
    /**
     * Returns the value of the given flag.
     * @param flag the flag id
     * @return boolean value
     */
    @Override
    public boolean getFlag(int flag) {
        return flags.get(flag);
    }
    /**
     * Puts an object to temp storage.
     *
     * @param id id
     * @param t  object
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T, R extends StorageCapableContext> R putToStorage(StorageId id, T t) {
        systemStorage.put(id, t);
        return (R) this;
    }
    /**
     * Gets an object from temp storage.
     *
     * @param id id
     * @return object
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getFromStorage(StorageId id) {
        return (T) systemStorage.get(id);
    }

    public <T> void putToUserStorage(String name, T t) {
        userStorage.put(name, t);
    }

    @SuppressWarnings("unchecked")
    public <T> T getFromUserStorage(String name) {
        return (T) userStorage.get(name);
    }
    /**
     * Returns true, if the key 'name' is set in user storage.
     *
     * @param name the name of the key
     * @return true, if set, false otherwise
     */
    public boolean isSetInUserStorage(String name) {
        return userStorage.containsKey(name);
    }
    /**
     * @return operation id
     */
    public String getOperationId() {
        if (isNull(operationId)) {
            operationId = IdUtils.v1String();
        }
        return operationId;
    }
    /**
     * Note: Can be set once!
     * @param operationId - operation id
     */
    public void setOperationId(String operationId) {
        if (isNull(this.operationId)) {
            this.operationId = operationId;
        }
    }

    void copyAllFromUserStorage(CommonRequestContext other) {
        this.userStorage.putAll(other.userStorage);
    }
    /**
     * Parent builder.
     *
     * @param <X>
     */
    public abstract static class CommonRequestContextBuilder<X extends CommonRequestContextBuilder<X>> implements InputCollector {
        /**
         * Operation id, special attribute for grouping, tracing, etc. actions.
         */
        private String operationId;
        /**
         * The "parent" context.
         */
        private CommonRequestContext parent;
        /**
         * Default constructor.
         */
        protected CommonRequestContextBuilder() {
            super();
        }
        /**
         * Copy constructor.
         * @param other the object to copy fields from
         */
        protected CommonRequestContextBuilder(CommonRequestContext other) {
            super();
            this.operationId = other.operationId;
        }
        /**
         * Sets this context's operation ID.
         * @param operationId the op. id
         * @return self
         */
        public X operationId(String operationId) {
            this.operationId = operationId;
            return self();
        }
        /**
         * Sets the "parent" context.
         * @param parent the parent context
         * @return self
         */
        public X parentContext(final CommonRequestContext parent) {
            this.parent = parent;
            return self();
        }
        /**
         * This cast trick.
         * @return self
         */
        @SuppressWarnings("unchecked")
        protected X self() {
            return (X) this;
        }
        /**
         * Build the object (overwritten in subclasses).
         * @return object
         */
        public abstract CommonRequestContext build();
    }
}
