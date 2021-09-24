package com.huahui.datasphere.mdm.system.serialization;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.variables.Variable.BooleanVariable;

/**
 * @author theseusyang
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
