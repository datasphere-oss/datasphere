package com.huahui.datasphere.mdm.system.serialization;

import java.util.Objects;

import com.huahui.datasphere.mdm.system.type.variables.Variable.StringVariable;

/**
 * @author theseusyang
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
