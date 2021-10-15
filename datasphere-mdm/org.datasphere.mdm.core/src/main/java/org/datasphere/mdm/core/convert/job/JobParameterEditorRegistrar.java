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
package org.datasphere.mdm.core.convert.job;

import org.datasphere.mdm.core.service.impl.job.JobParameterFactory;
import org.datasphere.mdm.core.type.job.JobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionBooleanParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionClobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionDateParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionInstantParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionIntegerParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionJobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionNumberParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionStringParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionTimeParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.collection.CollectionTimestampParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.custom.CustomJobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapBooleanParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapClobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapDateParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapInstantParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapIntegerParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapJobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapNumberParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapStringParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapTimeParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.map.MapTimestampParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleBooleanParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleClobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleDateParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleInstantParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleIntegerParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleJobParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleNumberParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleStringParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleTimeParameterDefinition;
import org.datasphere.mdm.core.type.job.impl.single.SingleTimestampParameterDefinition;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;

/**
 * @author Mikhail Mikhailov on Jul 11, 2021
 */
public class JobParameterEditorRegistrar implements PropertyEditorRegistrar {

    private static final Class<?>[] CLASSES = {

        JobParameterDefinition.class,

        CollectionBooleanParameterDefinition.class,
        CollectionClobParameterDefinition.class,
        CollectionDateParameterDefinition.class,
        CollectionInstantParameterDefinition.class,
        CollectionIntegerParameterDefinition.class,
        CollectionJobParameterDefinition.class,
        CollectionJobParameterDefinition.class,
        CollectionNumberParameterDefinition.class,
        CollectionStringParameterDefinition.class,
        CollectionTimeParameterDefinition.class,
        CollectionTimestampParameterDefinition.class,

        MapBooleanParameterDefinition.class,
        MapClobParameterDefinition.class,
        MapDateParameterDefinition.class,
        MapInstantParameterDefinition.class,
        MapIntegerParameterDefinition.class,
        MapJobParameterDefinition.class,
        MapNumberParameterDefinition.class,
        MapStringParameterDefinition.class,
        MapTimeParameterDefinition.class,
        MapTimestampParameterDefinition.class,

        SingleBooleanParameterDefinition.class,
        SingleClobParameterDefinition.class,
        SingleDateParameterDefinition.class,
        SingleInstantParameterDefinition.class,
        SingleIntegerParameterDefinition.class,
        SingleJobParameterDefinition.class,
        SingleNumberParameterDefinition.class,
        SingleStringParameterDefinition.class,
        SingleTimeParameterDefinition.class,
        SingleTimestampParameterDefinition.class,

        CustomJobParameterDefinition.class
    };

    private final JobParameterFactory jobParameterFactory;

    /**
     * Constructor.
     */
    public JobParameterEditorRegistrar(JobParameterFactory jobParameterFactory) {
        super();
        this.jobParameterFactory = jobParameterFactory;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {

        JobParameterDefinitionPropertyEditor editor = new JobParameterDefinitionPropertyEditor(jobParameterFactory);
        for (int i = 0;  i < CLASSES.length; i++) {
            registry.registerCustomEditor(CLASSES[i], editor);
        }
    }
}
