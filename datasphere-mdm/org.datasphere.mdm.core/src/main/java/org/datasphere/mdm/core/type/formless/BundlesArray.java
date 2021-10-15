package org.datasphere.mdm.core.type.formless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

/**
 * @author Mikhail Mikhailov on Sep 14, 2020
 * The class is mostly intended to ease serialization of formless data bundles.
 */
public class BundlesArray implements Iterable<DataBundle> {
    /**
     * Bundles.
     */
    private List<DataBundle> bundles = new ArrayList<>(8);
    /**
     * Constructor.
     */
    public BundlesArray() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<DataBundle> iterator() {
        return bundles.iterator();
    }
    /**
     * List iterator over bundles collection.
     * @return {@link ListIterator}.
     */
    public ListIterator<DataBundle> listIterator() {
        return bundles.listIterator();
    }
    /**
     * Stream of bundles.
     * @return stream
     */
    public Stream<DataBundle> stream() {
        return bundles.stream();
    }
    /**
     * Adds a bundle to array.
     * @param b the bundle to add
     * @return self
     */
    public BundlesArray add(DataBundle b) {

        if (Objects.nonNull(b)) {
            bundles.add(b);
        }

        return this;
    }
    /**
     * Adds several bundles to array.
     * @param b the bundle to add
     * @return self
     */
    public BundlesArray addAll(Collection<? extends DataBundle> bundles) {

        if (CollectionUtils.isNotEmpty(bundles)) {
            for (DataBundle b : bundles) {
                add(b);
            }
        }

        return this;
    }
    /**
     * Tells, if it is empty or not.
     * @return true, if empty, false otherwise
     */
    public boolean isEmpty() {
        return bundles.isEmpty();
    }
    /**
     * Gets the size of the bundles collection.
     * @return size
     */
    public int size() {
        return bundles.size();
    }
    /**
     * Clears the content of this bundle.
     */
    public void clear() {
        bundles.clear();
    }
}
