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

package org.datasphere.mdm.core.service.impl.job;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import org.datasphere.mdm.core.service.job.JobComplexParameterHolder;
import org.springframework.stereotype.Service;

/**
 * Object responsible fot containing complex parameters for job execution.
 */
@Service
public class JobComplexParameterHolderImpl implements JobComplexParameterHolder {

    /**
     * Map of complex parameters
     */
    private Map<String, Object> complexParameters = new ConcurrentHashMap<>();

    @Override
    public void putComplexParameter(@Nonnull String key, @Nonnull Object parameter) {
        complexParameters.put(key, parameter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getComplexParameter(@Nonnull String key) {
        return (T) complexParameters.get(key);
    }

    @Override
    public void removeComplexParameter(@Nonnull String key) {
        complexParameters.remove(key);
    }

    @Override
    public <T> T getComplexParameterAndRemove(@Nonnull String complexParameterKey) {
        T result = getComplexParameter(complexParameterKey);
        removeComplexParameter(complexParameterKey);
        return result;
    }
}
