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
package org.datasphere.mdm.core.context;

import org.datasphere.mdm.core.type.load.DataImportFormat;
import org.datasphere.mdm.system.context.CommonRequestContext;

/**
 * @author Mikhail Mikhailov on May 24, 2021
 */
public abstract class AbstractImportContext extends CommonRequestContext {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = -8901476050625307290L;
    /**
     * The target handler
     */
    private final String target;
    /**
     * The load format.
     */
    private final DataImportFormat format;
    /**
     * The entity name.
     */
    private final String entityName;
    /**
     * Constructor.
     * @param b
     */
    protected AbstractImportContext(AbstractImportContextBuilder<?> b) {
        super(b);
        this.target = b.target;
        this.format = b.format;
        this.entityName = b.entityName;
    }
    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }
    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }
    /**
     * @return the format
     */
    public DataImportFormat getFormat() {
        return format;
    }
    /**
     * @author Mikhail Mikhailov on May 13, 2021
     */
    public abstract static class AbstractImportContextBuilder<X extends AbstractImportContextBuilder<X>> extends CommonRequestContextBuilder<X> {
        /**
         * The target handler
         */
        private String target;
        /**
         * The load format.
         */
        private DataImportFormat format;
        /**
         * The entity name.
         */
        private String entityName;
        /**
         * Constructor.
         */
        protected AbstractImportContextBuilder() {
            super();
        }
        /**
         * Sets the target name.
         * @param target the target name
         * @return self
         */
        public X target(String target) {
            this.target = target;
            return self();
        }
        /**
         * Sets the format.
         * @param format the format
         * @return self
         */
        public X format(DataImportFormat format) {
            this.format = format;
            return self();
        }
        /**
         * @param entityName the entityName to set
         */
        public X entityName(String entityName) {
            this.entityName = entityName;
            return self();
        }
    }
}
