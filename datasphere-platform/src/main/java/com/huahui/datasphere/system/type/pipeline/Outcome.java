package com.huahui.datasphere.system.type.pipeline;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.system.util.TextUtils;

/**
 * The splitter/selector outcome definition.
 * @author Mikhail Mikhailov on May 24, 2020
 */
public final class Outcome {
    /**
     * Name (code).
     */
    private final String name;
    /**
     * Description. May be translation code.
     */
    private final String description;
    /**
     * ITC.
     */
    private final Class<? extends PipelineInput> inputTypeClass;
    /**
     * OTC.
     */
    private final Class<? extends PipelineOutput> outputTypeClass;
    /**
     * Constructor.
     * @param name the outcome name.
     * @param description its decription
     * @param inputTypeClass ITC
     * @param outputTypeClass OTC
     */
    public Outcome(String name, String description, Class<? extends PipelineInput> inputTypeClass, Class<? extends PipelineOutput> outputTypeClass) {
        super();
        this.name = name;
        this.description = description;
        this.inputTypeClass = inputTypeClass;
        this.outputTypeClass = outputTypeClass;
    }
    /**
     * Gets the direction name.
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Gets direction's description.
     * @return description
     */
    public String getDescription() {
        return Objects.nonNull(description) ? TextUtils.getText(description) : StringUtils.EMPTY;
    }
    /**
     * Returns the input type class.
     * @return the inputTypeClass
     */
    public Class<? extends PipelineInput> getInputTypeClass() {
        return inputTypeClass;
    }
    /**
     * Returns the output type class.
     * @return the outputTypeClass
     */
    public Class<? extends PipelineOutput> getOutputTypeClass() {
        return outputTypeClass;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, description, inputTypeClass, outputTypeClass);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Outcome other = (Outcome) obj;
        return Objects.equals(this.name, other.name)
            && Objects.equals(this.description, other.description)
            && Objects.equals(this.inputTypeClass, other.inputTypeClass)
            && Objects.equals(this.outputTypeClass, other.outputTypeClass);
    }
}
