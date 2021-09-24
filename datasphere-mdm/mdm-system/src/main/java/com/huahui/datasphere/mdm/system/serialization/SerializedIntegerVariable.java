package com.huahui.datasphere.mdm.system.serialization;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.variables.Variable.IntegerVariable;

/**
 * @author theseusyang
 */
public class SerializedIntegerVariable extends IntegerVariable implements SerializedVariable {
    /**
     * Constructor.
     */
    public SerializedIntegerVariable() {
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
