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
package org.datasphere.mdm.core.type.job;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.exception.JobException;

/**
 * @author Mikhail Mikhailov on Jun 19, 2021
 */
public abstract class JobParameterDefinition<X> {
    /**
     * Parameter real type.
     */
    private final JobParameterType type;
    /**
     * Parameter layout kind.
     */
    private final JobParameterKind kind;
    /**
     * Parameter name.
     */
    private final String name;
    /**
     * Constructor.
     * @param name the name
     * @param type the type
     */
    protected JobParameterDefinition(String name, JobParameterType type, JobParameterKind kind) {
        super();

        Objects.requireNonNull(name, "Parameter name must not be null.");
        Objects.requireNonNull(type, "Parameter data type must not be null.");
        Objects.requireNonNull(kind, "Parameter data layout must not be null.");

        this.name = name;
        this.type = type;
        this.kind = kind;
    }
    /**
     * Gets the parameter name.
     * @return parameter name
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the parameter type.
     * @return parameter type
     */
    public JobParameterType getType() {
        return type;
    }
    /**
     * @return the kind
     */
    public JobParameterKind getKind() {
        return kind;
    }
    /**
     * Tells, whether this parameter is a single value.
     * @return true, if this parameter is a single value
     */
    public boolean isSingle() {
        return false;
    }
    /**
     * Returns real value as definition type.
     * @return real value as definition type
     */
    public X single() {
        throw new JobException("Job parameter definition is not a single value.",
                CoreExceptionIds.EX_JOB_PARAMETER_IS_NOT_SINGLE);
    }
    /**
     * Tells, whether this parameter is a collection value.
     * @return true, if this parameter is a collection value
     */
    public boolean isCollection() {
        return false;
    }
    /**
     * Returns real value as a collection of values of definition type.
     * @return real value as a collection of values of definition type
     */
    public Collection<X> collection() {
        throw new JobException("Job parameter definition is not a collection value.",
                CoreExceptionIds.EX_JOB_PARAMETER_IS_NOT_COLLECTION);
    }
    /**
     * Tells, whether this parameter is a map value.
     * @return true, if this parameter is a map value
     */
    public boolean isMap() {
        return false;
    }
    /**
     * Returns real value as a map of values of definition type.
     * @return real value as a map of values of definition type
     */
    public Map<String, X> map() {
        throw new JobException("Job parameter definition is not a map value.",
                CoreExceptionIds.EX_JOB_PARAMETER_IS_NOT_MAP);
    }
    /**
     * Returns true, if this parameter is a custom value.
     * @return true, if this parameter is a custom value.
     */
    public boolean isCustom() {
        return false;
    }
    /**
     * Returns custom value.
     * @return custom value
     */
    public X asCustom() {
        throw new JobException("Job parameter definition is not a custom value.",
                CoreExceptionIds.EX_JOB_PARAMETER_IS_NOT_CUSTOM);
    }
    /**
     * Returns true, if this parameter is empty.
     * @return true, if this parameter is empty
     */
    public abstract boolean isEmpty();
}
