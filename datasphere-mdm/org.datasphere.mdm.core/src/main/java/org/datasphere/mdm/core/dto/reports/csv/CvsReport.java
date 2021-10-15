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

package org.datasphere.mdm.core.dto.reports.csv;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.datasphere.mdm.core.dto.reports.Report;

/**
 * Stateful class responsible for creating csv report.
 */
public class CvsReport implements Report<String> {

    /**
     * New row
     */
    private static final String NEW_ROW = "\n";

    /**
     * Separator
     */
    private final char separator;

    /**
     * CharSet
     */
    @Nonnull
    private final Charset charSet;

    /**
     * Current state of csv
     */
    private final StringBuffer result = new StringBuffer();

    /**
     * Constructor
     *
     * @param separator - csv separator
     * @param charSet   - returned charSet
     */
    public CvsReport(char separator, @Nullable String charSet) {
        this.separator = separator;
        this.charSet = charSet == null ? Charset.forName(StandardCharsets.UTF_8.name()) : Charset.forName(charSet);
    }

    /**
     * Constructor where will be default charset.
     *
     * @param separator - csv separator
     */
    public CvsReport(char separator) {
        this.separator = separator;
        this.charSet = Charset.forName(StandardCharsets.UTF_8.name());
    }

    @Override
    public void newRow() {
        result.append(NEW_ROW);
    }

    @Override
    public void addElement(@Nonnull String element) {
        String finalElement = element.replace(separator, ' ').replace(NEW_ROW, "");
        result.append(finalElement);
        result.append(separator);
    }

    @Nonnull
    @Override
    public byte[] generate() {
        return result.toString().getBytes(charSet);
    }
}
