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

package com.huahui.datasphere.mdm.core.util;

import java.io.InputStream;

import javax.ws.rs.core.StreamingOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huahui.datasphere.mdm.core.dto.LargeObjectResult;

/**
 * @author theseusyang
 */
public class LargeObjectUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LargeObjectUtils.class);
    /**
     * Disabling constructor.
     */
    private LargeObjectUtils() {
        super();
    }
    /**
     * Gets {@link StreamingOutput} for a context.
     *
     * @param result Large object
     * @return streaming output
     */
    public static StreamingOutput createStreamingOutputForLargeObject(final LargeObjectResult result) {
        return output -> {

            try (InputStream is = result.getInputStream()) {

                byte[] buf = new byte[FileUtils.DEFAULT_BUFFER_SIZE];
                int count = -1;
                while ((count = is.read(buf, 0, buf.length)) != -1) {
                    output.write(buf, 0, count);
                }
            } catch (Exception exc) {
                LOGGER.warn("Exception cought while BLOB I/O.", exc);
            }
        };
    }
}
