package com.huahui.datasphere.mdm.system.serialization;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.variables.Variable.FloatVariable;

/**
 * @author theseusyang
 */
public class SerializedFloatVariable extends FloatVariable implements SerializedVariable {
    /**
     * Constructor.
     */
    public SerializedFloatVariable() {
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
