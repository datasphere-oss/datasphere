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
package org.datasphere.mdm.core.type.load;
/**
 * @author Mikhail Mikhailov on May 13, 2021
 * For now, just designate the base input format.
 * The data in this format will hopefully be mapped analogously to ExchangeFormat for 5.x in the future.
 */
public enum DataImportFormat {
    /*
     * XML, Thrift, Avro, protobuff....
     */
    XLSX(".xlsx"),
    JSON(".json");
    /**
     * File extension.
     */
    private final String extension;
    /**
     * Constructor.
     * @param extension the file extension
     */
    private DataImportFormat(String extension) {
        this.extension = extension;
    }
    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }
}
