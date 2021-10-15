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

package org.datasphere.mdm.core.service.segments;

import org.datasphere.mdm.core.context.ModelGetContext;
import org.datasphere.mdm.core.dto.ModelGetResult;
import org.datasphere.mdm.core.module.CoreModule;
import org.springframework.stereotype.Component;
import org.datasphere.mdm.system.type.pipeline.Start;
/**
 * @author Mikhail Mikhailov on Nov 28, 2019
 */
@Component(ModelGetStartExecutor.SEGMENT_ID)
public class ModelGetStartExecutor extends Start<ModelGetContext, ModelGetResult> {
    /**
     * This segment ID.
     */
    public static final String SEGMENT_ID = CoreModule.MODULE_ID + "[MODEL_GET_START]";
    /**
     * Localized message code.
     */
    public static final String SEGMENT_DESCRIPTION = CoreModule.MODULE_ID + ".model.get.start.description";
    /**
     * Constructor.
     */
    public ModelGetStartExecutor() {
        super(SEGMENT_ID, SEGMENT_DESCRIPTION, ModelGetContext.class, ModelGetResult.class);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void start(ModelGetContext ctx) {
        // NOOP. Start does nothing here.
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String subject(ModelGetContext ctx) {
        // No subject for this type of pipelines
        // This may be storage id in the future
        return null;
    }
}
