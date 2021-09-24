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

/**
 *
 */
package com.huahui.datasphere.mdm.core.dto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;

import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;
import com.huahui.datasphere.mdm.core.type.lob.LargeObjectAcceptance;
import com.huahui.datasphere.mdm.core.util.FileUtils;

/**
 * @author theseusyang
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
