package com.huahui.datasphere.mdm.core.type.job;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import com.huahui.datasphere.mdm.system.type.job.ModularBatchJobFraction;

/**
 * A simple holder for BJ fractions.
 * @author theseusyang on Jan 18, 2020
 */
public class ModularBatchJobSupport implements Iterable<ModularBatchJobFraction> {
    /**
     * The fractions.
     */
    private final List<ModularBatchJobFraction> fractions;
    /**
     * Constructor.
     * @param fractions for this support. May be null or empty
     */
    public ModularBatchJobSupport(@Nullable List<ModularBatchJobFraction> fractions) {
        this.fractions = CollectionUtils.isEmpty(fractions) ? Collections.emptyList() : fractions;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<ModularBatchJobFraction> iterator() {
        return fractions.iterator();
    }
}
