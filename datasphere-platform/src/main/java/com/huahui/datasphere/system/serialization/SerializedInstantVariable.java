package com.huahui.datasphere.system.serialization;

import java.util.Objects;

import com.huahui.datasphere.system.type.variables.Variable.InstantVariable;

/**
 * @author Mikhail Mikhailov on Sep 13, 2020
 */
public class SerializedInstantVariable extends InstantVariable implements SerializedVariable {
    /**
     * Constructor.
     */
    public SerializedInstantVariable() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return Objects.nonNull(getName());
    }
    /**
     * @param name the name to set
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
}
