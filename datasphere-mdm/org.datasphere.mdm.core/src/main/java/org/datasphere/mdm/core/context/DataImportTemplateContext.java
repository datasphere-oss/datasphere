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

/**
 * @author Mikhail Mikhailov on May 24, 2021
 */
public class DataImportTemplateContext extends AbstractImportContext {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 5690466419767540260L;
    /**
     * Constructor.
     * @param b
     */
    private DataImportTemplateContext(DataImportTemplateContextBuilder b) {
        super(b);
    }
    /**
     * Builder.
     * @return builder
     */
    public static DataImportTemplateContextBuilder builder() {
        return new DataImportTemplateContextBuilder();
    }
    /**
     * @author Mikhail Mikhailov on May 24, 2021
     */
    public static class DataImportTemplateContextBuilder extends AbstractImportContextBuilder<DataImportTemplateContextBuilder> {
        /**
         * Constructor.
         */
        private DataImportTemplateContextBuilder() {
            super();
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public DataImportTemplateContext build() {
            return new DataImportTemplateContext(this);
        }
    }
}
