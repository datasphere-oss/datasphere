package com.huahui.datasphere.mdm.system.type.pipeline;

/**
 * The splitter segment type.
 * @author theseusyang on May 24, 2020
 */
public abstract class Selector<C extends PipelineInput, O extends PipelineOutput> extends OutcomesSegment<C, O> {
    /**
     * Constructor.
     * @param id
     * @param description
     */
    protected Selector(String id, String description, Class<O> outputTypeClass, Outcome... outcomes) {
        super(id, description, outputTypeClass, outcomes);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public SegmentType getType() {
        return SegmentType.SELECTOR;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBatched() {
        return false;
    }
    /**
     * Evaluates current context and defines the outcome direction.
     * @param ctx the context
     * @return the split direction
     */
    public abstract Outcome select(C ctx);
    /**
     * If selected outcome's pipeline requires an input type other then {@link C} this is the place,
     * where input can be converted to this required type. Default implementation just returns the input.
     * @param ctx the input
     * @param outcome the selected outcome
     * @return pipeline input, suitable for the pipeline, connected to the outcome
     */
    public PipelineInput in(C ctx, Outcome outcome) {
        return ctx;
    }
    /**
     * If selected outcome's pipeline returns a type other then {@link O} this is the place,
     * where output can be coverted to O type. Default implementation just casts the output.
     * @param x the execution result
     * @param outcome the selected outcome
     * @return result, converted to {@link O}
     */
    @SuppressWarnings("unchecked")
    public O out(PipelineOutput x, Outcome outcome) {
        return (O) x;
    }
}
