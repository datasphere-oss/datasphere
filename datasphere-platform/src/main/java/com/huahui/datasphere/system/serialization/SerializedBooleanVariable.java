package com.huahui.datasphere.system.serialization;

import java.util.Objects;

import com.huahui.datasphere.system.type.variables.Variable.BooleanVariable;

/**
 * @author Mikhail Mikhailov on Sep 13, 2020
 */
public class SerializedBooleanVariable extends BooleanVariable implements SerializedVariable {
    /**
     * Constructor.
     */
    public SerializedBooleanVariable() {
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
