package com.huahui.datasphere.system.serialization;

import java.util.Objects;

import com.huahui.datasphere.system.type.variables.Variable.StringVariable;

/**
 * @author Mikhail Mikhailov on Sep 13, 2020
 */
public class SerializedStringVariable extends StringVariable implements SerializedVariable {
    /**
     * Constructor.
     */
    public SerializedStringVariable() {
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
    public void setName(String name) {
        this.name = name;
    }
}
