/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.mdm.core.type.job;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import org.springframework.batch.core.JobParameter;

/**
 * Custom job parameter used only as wrapper on base class {@link JobParameter} to add arrays.
 * All work based on reflection mechanism.
 *
 * @author Aleksandr Magdenko
 */
public class CustomJobParameter extends JobParameter {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -8116683696740383807L;

    public CustomJobParameter(String[] parameter) {
        super((String)null);
        setParameterValue(parameter);
    }

    public CustomJobParameter(Long[] parameter) {
        super((Long)null);
        setParameterValue(parameter);
    }

    public CustomJobParameter(Date[] parameter) {
        super((Date)null);
        setParameterValue(parameter);
    }

    public CustomJobParameter(Double[] parameter) {
        super((Double)null);
        setParameterValue(parameter);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof JobParameter)) {
            return false;
        } else if (this == obj) {
            return true;
        } else {
            JobParameter rhs = (JobParameter)obj;
            if (getValue() == null) {
                return rhs.getValue() == null && this.getType() == rhs.getType();
            } else {
                if (getValue().getClass().isArray()) {
                    if (rhs.getValue() != null && rhs.getValue().getClass().isArray()) {
                        // Note, that we can't get primitive arrays here. Hence no need to check for primitive arrays.
                        return Arrays.equals((Object[])getValue(), (Object[])rhs.getValue());
                    } else {
                        return false;
                    }
                } else {
                    return this.getValue().equals(rhs.getValue());
                }
            }
        }
    }

    @Override
    public String toString() {
        if (this.getValue() == null) {
            return null;
        }

        if (this.getValue().getClass().isArray()) {
            return Arrays.asList(this.getValue()).toString();
        } else {
            return super.toString();
        }
    }

    @Override
    public int hashCode() {
        return 7 + 21 * (this.getValue() == null ? this.getType().hashCode() :
                (getValue().getClass().isArray() ?
                        // Note, that we can't get primitive arrays here. Hence no need to check for primitive arrays.
                        Arrays.hashCode((Object[]) getValue()) : getValue().hashCode()));
    }

    /**
     * This is hack method to set array directly in parent class.
     *
     * @param object Object value can be array.
     */
    private void setParameterValue(Object object) {
        try {
            Field parameterField = JobParameter.class.getDeclaredField("parameter");
            parameterField.setAccessible(true);

            parameterField.set(this, object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to instantiate job parameter", e);
        }
    }
}
