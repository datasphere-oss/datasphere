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

package org.datasphere.mdm.core.util;

import java.io.InputStream;

import javax.ws.rs.core.StreamingOutput;

import org.datasphere.mdm.core.dto.LargeObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexander Malyshev
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
