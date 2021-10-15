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

/**
 *
 */
package org.datasphere.mdm.core.dto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.type.lob.LargeObjectAcceptance;
import org.datasphere.mdm.core.util.FileUtils;
import org.datasphere.mdm.system.exception.PlatformFailureException;

/**
 * @author Mikhail Mikhailov
 * LOB result object.
 */
public class LargeObjectResult {
    /**
     * 'Delete on close' input stream.
     */
    private InputStream inputStream;
    /**
     * Object id.
     */
    private String id;
    /**
     * File name.
     */
    private String fileName;
    /**
     * MIME type.
     */
    private String mimeType;
    /**
     * Size.
     */
    private long size;
    /**
     * State.
     */
    private LargeObjectAcceptance acceptance;
    /**
     * Constructor.
     */
    public LargeObjectResult() {
        super();
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param inputStream the inputStream to set
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    /**
     * @return the inputStream
     */
    public InputStream getInputStream() {
        return inputStream;
    }
    /**
     * Gets the bytes from LOB input stream.
     * The LOB IS is not available for reading after that.
     * @return bytes
     */
    public byte[] getBytes() {

        if (Objects.isNull(inputStream)) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             InputStream is = inputStream;) {

            int count;
            byte[] buf = new byte[FileUtils.DEFAULT_BUFFER_SIZE];
            while ((count = is.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, count);
            }

            return baos.toByteArray();

        } catch (IOException e) {
            throw new PlatformFailureException(
                    "Failed to read input from LOB.",
                    CoreExceptionIds.EX_LOB_RESULT_READ_FAILED, e);
        }
    }
    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }
    /**
     * @param size the size to set
     */
    public void setSize(long size) {
        this.size = size;
    }
    /**
     * @return the size
     */
    public long getSize() {
        return size;
    }
    /**
     * @return the acceptance
     */
    public LargeObjectAcceptance getAcceptance() {
        return acceptance;
    }
    /**
     * @param acceptance the acceptance to set
     */
    public void setAcceptance(LargeObjectAcceptance acceptance) {
        this.acceptance = acceptance;
    }
}
