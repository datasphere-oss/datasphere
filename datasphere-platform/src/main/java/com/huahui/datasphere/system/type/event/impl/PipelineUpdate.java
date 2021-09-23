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

package com.huahui.datasphere.system.type.event.impl;

import com.huahui.datasphere.system.type.event.AbstractForeignEvent;
import com.huahui.datasphere.system.util.IdUtils;

/**
 * @author Mikhail Mikhailov on Nov 26, 2019
 */
public class PipelineUpdate extends AbstractForeignEvent {
    /**
     * Type of the operation.
     * @author Mikhail Mikhailov on Nov 27, 2019
     */
    public enum PipelineUpdateType {
        UPSERT,
        REMOVAL
    }
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 9017170992401345410L;
    /**
     * This type name.
     */
    private static final String TYPE_NAME = "PIPELINE_UPDATE_EVENT";
    /**
     * The start id.
     */
    private String startId;
    /**
     * The subject id;
     */
    private String subjectId;
    /**
     * The update type.
     */
    private PipelineUpdateType updateType;
    /**
     * Constructor.
     */
    public PipelineUpdate() {
        super(TYPE_NAME, IdUtils.v1String());
    }
    /**
     * @return the startId
     */
    public String getStartId() {
        return startId;
    }
    /**
     * @param startId the startId to set
     */
    public void setStartId(String startId) {
        this.startId = startId;
    }
    /**
     * @return the subjectId
     */
    public String getSubjectId() {
        return subjectId;
    }
    /**
     * @param subjectId the subjectId to set
     */
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
    /**
     * @return the updateType
     */
    public PipelineUpdateType getUpdateType() {
        return updateType;
    }
    /**
     * @param updateType the updateType to set
     */
    public void setUpdateType(PipelineUpdateType updateType) {
        this.updateType = updateType;
    }
}
