package com.huahui.datasphere.mdm.system.serialization.protostuff;

import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;

import io.protostuff.Output;

/**
 * @author theseusyang
 * Some utilities for output support.
 */
public interface OutpuMappingSupport {
    /**
     * Writes an array to, using the given mapper.
     * @param output the output to write to.
     * @param values the values to write
     * @param mapper the mapper
     */
    default void writeArrayTo(Output output, Object[] values, ArrayElementMapper<Output, Object> mapper) throws IOException {

        if (ArrayUtils.isEmpty(values)) {
            return;
        }

        for (int i = 0; i < values.length; i++) {
            mapper.map(output, values[i]);
        }
    }
    /**
     * @author theseusyang on Sep 30, 2020
     * The Bi-mapper, throwing {@link IOException}.
     */
    @FunctionalInterface
    public interface ArrayElementMapper<T, U> {
        /**
         * {@inheritDoc}
         */
        void map(T t, U u) throws IOException;
    }
}
