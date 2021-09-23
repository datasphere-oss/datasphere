

package com.huahui.datasphere.system.context;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import com.huahui.datasphere.system.type.pipeline.fragment.InputFragment;

/**
 * @author theseusyang
 * The fragments holder.
 */
public final class InputFragmentHolder {
    /**
     * Single context was supplied for id.
     */
    private final InputFragment<?> single;
    /**
     * Multiple contexts were supplied for id.
     */
    private final Collection<InputFragment<?>> multiple;
    /**
     * Constructor.
     * @param single the fragment
     */
    private InputFragmentHolder(InputFragment<?> single) {
        super();
        this.single = single;
        this.multiple = null;
    }
    /**
     * Constructor.
     * @param multiple contexts
     */
    private InputFragmentHolder(Collection<? extends InputFragment<?>> multiple) {
        super();
        this.single = null;
        this.multiple = CollectionUtils.isEmpty(multiple) ? Collections.emptyList() : List.copyOf(multiple);
    }
    /**
     * @return the single
     */
    public InputFragment<?> getSingle() {
        return single;
    }
    /**
     * @return the multiple
     */
    public Collection<InputFragment<?>> getMultiple() {
        return multiple;
    }
    /**
     * Check for having a single fragment for id.
     * @return true for single, false otherwise
     */
    public boolean isSingle() {
        return single != null && multiple == null;
    }
    /**
     * Check for having multiple fragments for id.
     * @return true for multiple, false otherwise
     */
    public boolean isMultiple() {
        return single == null && multiple != null;
    }
    /**
     * Returns elements hold as stream.
     * @return stream
     */
    public Stream<InputFragment<?>> stream() {

        if (isSingle()) {
            return Stream.ofNullable(getSingle());
        }

        return CollectionUtils.isEmpty(getMultiple())
                ? Stream.empty()
                : getMultiple().stream();
    }
    /**
     * Creates holder instance.
     * @param single the fragment
     * @return holder
     */
    public static InputFragmentHolder of(InputFragment<?> single) {
        return new InputFragmentHolder(single);
    }
    /**
     * Creates holder instance.
     * @param multiple fragments
     * @return holder
     */
    public static InputFragmentHolder of(Collection<? extends InputFragment<?>> multiple) {
        return new InputFragmentHolder(multiple);
    }
}