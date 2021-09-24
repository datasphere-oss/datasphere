package com.huahui.datasphere.mdm.system.type.pipeline;

import java.util.Map;

/**
 * The splitter segment type.
 * @author theseusyang on May 24, 2020
 */
public abstract class Splitter<C extends PipelineInput, O extends PipelineOutput> extends OutcomesSegment<C, O> {
    /**
     * Constructor.
     * @param id
     * @param description
     */
    protected Splitter(String id, String description, Class<O> outputTypeClass, Outcome... outcomes) {
        super(id, description, outputTypeClass, outcomes);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SegmentType getType() {
        return SegmentType.SPLITTER;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBatched() {
        return false;
    }
    /**
     * Evaluates current context and defines outcomes, that should be executed.
     * Default implementation just returns {@link #getOutcomes()} so all connected pipelines will be executed.
     * @param ctx the context
     * @return the split directions
     */
    public Outcome[] split(C ctx) {
        return getOutcomes();
    }
    /**
     * Combines execution results of different outcomes into a single output.
     * Default implementation just return null
     * @param collected the results map
     * @return
     */
    public O join(Map<Outcome, ? extends PipelineOutput> collected) {
        return null;
    }
    /**
     * If selected outcome's pipeline requires an input type other then {@link C} - this is the place,
     * where input can be converted to this required type. Default implementation just returns the input.
     * @param ctx the input
     * @param outcome the selected outcome
     * @return pipeline input, suitable for the pipeline, connected to the outcome
     */
    public PipelineInput in(C ctx, Outcome outcome) {
        return ctx;
    }
}
