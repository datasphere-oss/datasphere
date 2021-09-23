

package com.huahui.datasphere.system.context;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import com.huahui.datasphere.system.type.pipeline.fragment.FragmentId;
import com.huahui.datasphere.system.type.pipeline.fragment.InputFragment;
import com.huahui.datasphere.system.type.pipeline.fragment.InputFragmentCollector;
import com.huahui.datasphere.system.type.pipeline.fragment.InputFragmentContainer;

/**
 * @author theseusyang
 * Composite context.
 */
public class AbstractCompositeRequestContext extends CommonRequestContext implements InputFragmentContainer {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = 2683127071228358568L;
    /**
     * Fragments map.
     */
    protected final transient Map<FragmentId<? extends InputFragment<?>>, InputFragmentHolder> fragments;
    /**
     * Constructor.
     * @param b
     */
    public AbstractCompositeRequestContext(AbstractCompositeRequestContextBuilder<?> b) {
        super(b);
        this.fragments = b.fragments;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends InputFragment<C>> C fragment(FragmentId<C> f) {

        if (MapUtils.isEmpty(fragments)) {
            return null;
        }

        InputFragmentHolder h = fragments.get(f);
        if (Objects.isNull(h) || !h.isSingle()) {
            return null;
        }

        return (C) h.getSingle();
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <C extends InputFragment<C>> Collection<C> fragments(FragmentId<C> f) {

        if (MapUtils.isEmpty(fragments)) {
            return Collections.emptyList();
        }

        InputFragmentHolder h = fragments.get(f);
        if (Objects.isNull(h) || !h.isMultiple()) {
            return Collections.emptyList();
        }

        return (Collection<C>) h.getMultiple();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<InputFragmentHolder> fragments() {

        if (MapUtils.isEmpty(fragments)) {
            return Collections.emptyList();
        }

        return fragments.values();
    }
    /**
     * Parent builder.
     * @author Mikhail Mikhailov
     *
     * @param <X>
     */
    public abstract static class AbstractCompositeRequestContextBuilder<X extends AbstractCompositeRequestContextBuilder<X>>
        extends CommonRequestContextBuilder<X> implements InputFragmentCollector<X> {
        /**
         * Fragments map.
         */
        private Map<FragmentId<? extends InputFragment<?>>, InputFragmentHolder> fragments;
        /**
         * Default constructor.
         */
        protected AbstractCompositeRequestContextBuilder() {
            super();
        }
        /**
         * Copy constructor.
         * @param other the object to copy fields from
         */
        protected AbstractCompositeRequestContextBuilder(AbstractCompositeRequestContext other) {
            super(other);
            this.fragments = other.fragments;
        }
        /**
         * Adds a singleton fragment for an ID using a supplier.
         * @param s the supplier
         * @return self
         */
        @Override
        public X fragment(Supplier<? extends InputFragment<?>> s) {

            InputFragment<?> f = s.get();
            if (Objects.nonNull(f)) {

                if (fragments == null) {
                    fragments = new IdentityHashMap<>();
                }

                fragments.put(f.fragmentId(), InputFragmentHolder.of(f));
            }
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public X fragment(InputFragment<?> f) {
            return fragment(() -> f);
        }
        /**
         * Adds a fragment collection for an ID using a supplier.
         * @param s the supplier
         * @return self
         */
        @Override
        public X fragments(Supplier<Collection<? extends InputFragment<?>>> s) {

            Collection<? extends InputFragment<?>> fs = s.get();
            if (Objects.nonNull(fs) && CollectionUtils.isNotEmpty(fs)) {

                if (fragments == null) {
                    fragments = new IdentityHashMap<>();
                }

                fragments.put(fs.iterator().next().fragmentId(), InputFragmentHolder.of(fs));
            }
            return self();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public X fragments(Collection<? extends InputFragment<?>> f) {
            return fragments(() -> f);
        }
    }
}
