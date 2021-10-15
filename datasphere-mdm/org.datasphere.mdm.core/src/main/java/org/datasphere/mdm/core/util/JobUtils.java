/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.util;

import java.util.Collection;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.datasphere.mdm.core.service.impl.job.JobParameterFactory;
import org.datasphere.mdm.core.service.job.CustomJobRegistry;
import org.datasphere.mdm.core.type.job.JobDescriptor;
import org.datasphere.mdm.core.type.job.JobFraction;
import org.datasphere.mdm.core.type.job.JobParameterDefinition;
import org.datasphere.mdm.core.type.job.JobParameterDescriptor;
import org.datasphere.mdm.core.type.job.StepStorage;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.datasphere.mdm.system.configuration.ModuleConfiguration;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Mikhail Mikhailov on Dec 19, 2019
 */
public class JobUtils {
     /**
     * All.
     */
    public static final String JOB_ALL = "ALL";
    /**
     * Partition mark.
     */
    public static final String JOB_PARTITION = "partition:";
    /**
     * Member UUID.
     */
    public static final String JOB_CLUSTER_MEMBER_UUID = "memberUUID";
    /**
     * Step's static parameters.
     */
    private static ThreadLocal<StepStorage> stepStateStorage = new ThreadLocal<>();
    /**
     * The CJR bean.
     */
    private static CustomJobRegistry jobRegistry;
    /**
     * The JPF bean.
     */
    private static JobParameterFactory jobParameterFactory;
    /**
     * Constructor.
     */
    private JobUtils() {
        super();
    }
    /**
     * Traditional static init.
     */
    public static void init() {
        jobRegistry = ModuleConfiguration.getBean(CustomJobRegistry.class);
        jobParameterFactory = ModuleConfiguration.getBean(JobParameterFactory.class);
    }
    /**
     * Gets descriptor for the given job name.
     * @param jobName the job name
     * @return descriptor or null, if the name is unknown
     */
    @Nullable
    public static JobDescriptor getDescriptor(String jobName) {
        return jobRegistry.getDescriptor(jobName);
    }
    /**
     * Gets the names of jobs currently registered.
     * @return names collection
     */
    @Nonnull
    public static Set<String> getDescriptorNames() {
        return jobRegistry.getDescriptorNames();
    }
    /**
     * Gets fractions, that may have been registered for the given job name.
     * @param jobName the job name
     * @return collection of fractions
     */
    @Nonnull
    public static Collection<JobFraction> getFractions(String jobName) {
        return jobRegistry.getFractions(jobName);
    }
    /**
     * Provides the currently registered job names. The return value is
     * unmodifiable and disconnected from the underlying registry storage.
     *
     * @return a collection of String. Empty if none are registered.
     */
    public static Collection<String> getJobNames() {
        return jobRegistry.getJobNames();
    }
    /**
     * Locates a {@link Job} at runtime.
     *
     * @param name the name of the {@link Job} which should be
     * unique
     * @return a {@link Job} identified by the given name
     *
     * @throws NoSuchJobException if the required configuration can
     * not be found.
     */
    public static Job getJob(@Nullable String name) throws NoSuchJobException {
        return jobRegistry.getJob(name);
    }
    /**
     * Marshals a parameter in SB format (with job name). Job name is required.
     * @param jobName the job name
     * @param parameter the parameter
     * @return string
     */
    public static String toString(String jobName, JobParameterDefinition<?> parameter) {
        return jobParameterFactory.toString(jobName, parameter);
    }
    /**
     * Reads and restores (unmarshals) parameter value from string in SB format.
     * @param parameterAsString the JSON text
     * @return parameter or null
     */
    public static<X> JobParameterDefinition<X> fromString(String parameterAsString) {
        return jobParameterFactory.fromString(parameterAsString);
    }
    /**
     * Extracts parameter's value from the {@link JsonNode} using the supplied parameter descriptor.
     * @param d the descriptor
     * @param value the value
     * @return value as object (single value, collection, map or custom value)
     */
    public static Object fromNode(JobParameterDescriptor<?> d, JsonNode n) {
        return jobParameterFactory.fromNode(d, n);
    }
    /**
     * Creates a new parameter definition using supplied value and parameter descriptor.
     * The values are expected to be strictly of the type and kind, declared by descriptor or null.
     * This is responsibility of either deserializer or parameter default value supplier.
     * The caller will get CCE, if this can not be ensured.
     * @param d the descriptor
     * @param value the value to cast and turn into a {@link JobParameterDefinition}.
     * @return parameter definition or null
     */
    public static<X> JobParameterDefinition<X> fromValue(JobParameterDescriptor<X> d, Object value) {
        return jobParameterFactory.fromValue(d, value);
    }
    /**
     * Generates partition name. Just an int to string for now.
     * @param i partition number
     * @return name
     */
    public static String partitionName(int i) {
        return JOB_PARTITION + Integer.toString(i);
    }
    /**
     * Generates partition name. Just an int to string for now.
     * @param i partition number
     * @return name
     */
    public static String targetedPartitionName(int i, String targetUUID) {
        return JOB_PARTITION + Integer.toString(i) + ":" + targetUUID;
    }
    /**
     * Reference name constructor.
     * @param runId the run id
     * @param objectName the object name
     * @return name
     */
    public static String getObjectReferenceName(String runId, String objectName) {

        return new StringBuilder()
                .append(runId)
                .append("_")
                .append(objectName)
                .toString();
    }
    public static String getObjectReferenceName(String runId, String objectName, String objectDetails) {

        return new StringBuilder()
                .append(runId)
                .append("_")
                .append(objectName)
                .append("_")
                .append(objectDetails)
                .toString();
    }
    /**
     * Gets staep execution state object.
     * @return state object
     */
    @SuppressWarnings("unchecked")
    public static<T extends StepStorage> T getStepState() {
        return (T) stepStateStorage.get();
    }
    /**
     * Sets step state object.
     * @param eo the object to set
     */
    public static void setStepState(StepStorage state) {
        stepStateStorage.set(state);
    }
    /**
     * Removes current step state object.
     */
    @SuppressWarnings("unchecked")
    public static<T extends StepStorage> T removeStepState() {
        T t = (T) stepStateStorage.get();
        stepStateStorage.remove();
        return t;
    }
}
