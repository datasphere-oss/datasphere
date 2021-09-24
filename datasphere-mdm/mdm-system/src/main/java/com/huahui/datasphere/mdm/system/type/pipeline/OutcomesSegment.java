package com.huahui.datasphere.mdm.system.type.pipeline;

/**
 * @author theseusyang on Jan 12, 2021
 * Abstract segment, holding other pipelines execution outcomes.
 */
public abstract class OutcomesSegment<C extends PipelineInput, O extends PipelineOutput> extends Segment {
    /**
     * The exact output type class.
     */
    private final Class<O> outputTypeClass;
    /**
     * The outcomes, known to this splitter.
     */
    private final Outcome[] outcomes;
    /**
     * Constructor.
     * @param id
     * @param description
     * @param output type
     * @param outcomes descriptors
     */
    protected OutcomesSegment(String id, String description, Class<O> outputTypeClass, Outcome... outcomes) {
        super(id, description);
        this.outputTypeClass = outputTypeClass;
        this.outcomes = outcomes;
    }
    /**
     * @return the outputTypeClass
     */
    public Class<O> getOutputTypeClass() {
        return outputTypeClass;
    }
    /**
     * @return the outcomes
     */
    public Outcome[] getOutcomes() {
        return outcomes;
    }
}
