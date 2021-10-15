package org.datasphere.mdm.core.convert.job;

import java.beans.PropertyEditorSupport;

import org.datasphere.mdm.core.service.impl.job.JobParameterFactory;
import org.datasphere.mdm.core.type.job.JobParameterDefinition;

/**
 * @author Mikhail Mikhailov on Jul 11, 2021
 */
public class JobParameterDefinitionPropertyEditor extends PropertyEditorSupport {

    private final JobParameterFactory parameterFactory;

    /**
     * Constructor.
     */
    public JobParameterDefinitionPropertyEditor(JobParameterFactory parameterFactory) {
        super();
        this.parameterFactory = parameterFactory;
    }
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public String getAsText() {
        return parameterFactory.toString(null, (JobParameterDefinition) super.getValue());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        super.setValue(parameterFactory.fromString(text));
    }
}
