package com.huahui.datasphere.mdm.system.serialization;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.variables.Variable.TimeVariable;

/**
 * @author theseusyang
 */
public class SerializedTimeVariable extends TimeVariable implements SerializedVariable {
    /**
     * Constructor.
     */
    public SerializedTimeVariable() {
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
