package org.datasphere.mdm.core.type.model;

/**
 * @author Mikhail Mikhailov on Oct 20, 2020
 */
public interface IndexingParamsElement {
    /**
     * The element supports indexing and search with regard to language morphology.
     * @return true, if supports morphological indexing and search
     */
    boolean isMorphological();
    /**
     * The element supports case insensitive indexing and search.
     * @return true, if supports case insensitive indexing and search
     */
    boolean isCaseInsensitive();
}
