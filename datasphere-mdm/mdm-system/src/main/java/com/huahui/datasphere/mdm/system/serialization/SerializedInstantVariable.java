package com.huahui.datasphere.mdm.system.serialization;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.variables.Variable.InstantVariable;

/**
 * @author theseusyang
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
