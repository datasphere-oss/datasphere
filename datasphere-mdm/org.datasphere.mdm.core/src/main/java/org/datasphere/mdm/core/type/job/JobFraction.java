package org.datasphere.mdm.core.type.job;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Basic interface, which allowes optional modules participate on optional batch jobs.
 * Concrete support instances must be marked with annotation {@literal @}JobRef annotation, i. e.
 * {@literal @}JobRef("reindexDataJob").
 * @author Mikhail Mikhailov on Feb 17, 2020
 */
public abstract class JobFraction {
    /**
     * Default execution order
     */
    private static final int DEFAULT_EXECUTION_ORDER = 0;
    /**
     * This fraction ID.
     */
    private final String id;
    /**
     * The parameters.
     */
    private final Map<String, JobParameterDescriptor<?>> parameters;
    /**
     * Constructor.
     * @param id fraction id
     * @param parameters the optional parameters
     */
    protected JobFraction(String id, Collection<JobParameterDescriptor<?>> parameters) {
        super();
        Objects.requireNonNull(id, "Job fraction ID must not be null.");
        this.id = id;
        this.parameters = Objects.isNull(parameters)
                ? Collections.emptyMap()
                : parameters.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(JobParameterDescriptor::getName, Function.identity()));
    }
    /**
     * Gets this fraction ID.
     * @return ID
     */
    public String getId() {
        return id;
    }
    /**
     * Gets this fraction display name
     * @return display name
     */
    public abstract String getDisplayName();
    /**
     * Gets this fraction description.
     * @return fraction description
     */
    public abstract String getDescription();
    /**
     * Gets parameters as collection.
     * @return parameters
     */
    public Collection<JobParameterDescriptor<?>> getParametersCollection() {
        return Objects.isNull(parameters) ? Collections.emptyList() : parameters.values();
    }
    /**
     * Gets parameters as map.
     * @return parameters
     */
    public Map<String, JobParameterDescriptor<?>> getParametersMap() {
        return Objects.isNull(parameters) ? Collections.emptyMap() : parameters;
    }
    /**
     * Finds a parameter by name.
     * @param name the parameter name
     * @return parameter or null
     */
    @Nullable
    public JobParameterDescriptor<?> findParameter(String name) {
        return parameters.get(name);
    }
    /**
     * May implement and return a job execution listener,
     * that will be executed according to the fractions order.
     * @return listener or null
     */
    @Nullable
    public JobExecutionListener jobExecutionListener() {
        return null;
    }
    /**
     * May implement and return a step execution listener for the given step name,
     * that will be executed according to the fractions order.
     * @param stepName the step name
     * @return listener or null
     */
    @Nullable
    public StepExecutionListener stepExecutionListener(String stepName) {
        return null;
    }
    /**
     * May implement and return an item read listener for the given step name,
     * that will be executed according to the fractions order.
     * @param stepName the step name
     * @return listener or null
     */
    @Nullable
    public ItemReadListener<?> itemReadListener(String stepName) {
        return null;
    }
    /**
     * May implement and return an item process listener for the given step name,
     * that will be executed according to the fractions order.
     * @param stepName the step name
     * @return listener or null
     */
    @Nullable
    public ItemProcessListener<?, ?> itemProcessListener(String stepName) {
        return null;
    }
    /**
     * May implement and return an item write listener for the given step name,
     * that will be executed according to the fractions order.
     * @param stepName the step name
     * @return listener or null
     */
    @Nullable
    public ItemWriteListener<?> itemWriteListener(String stepName) {
        return null;
    }
    /**
     * If the are several post processors for the same job, the order of execution will be ascending order according to the value,
     * returned from this method.
     * @return
     */
    public int getOrder() {
        return DEFAULT_EXECUTION_ORDER;
    }
}
