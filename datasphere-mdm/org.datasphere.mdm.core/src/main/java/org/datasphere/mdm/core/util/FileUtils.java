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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.system.exception.PlatformFailureException;

/**
 * @author Michael Yashin. Created on 21.05.2015.
 */
public class FileUtils {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * The Constant TO_IMPORT.
     */
    private static final String TO_IMPORT = "to_import";

    /**
     * The Constant TO_EXPORT.
     */
    private static final String TO_EXPORT = "to_export";

    /**
     * The Constant TEMP.
     */
    private static final String TEMP = "temp";

    /**
     * The Constant CATALINA_BASE.
     */
    private static final String CATALINA_BASE = "catalina.base";

    /**
     * The Constant DELIMETER.
     */
    private static final String DELIMETER = "_";

    /**
     * The Constant FILENAME.
     */
    private static final String FILENAME = "filename";

    /**
     * The Constant SDF.
     */
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-M-yyyy_hh_mm_ss");

    public static String readFile(String path, Charset encoding) throws IOException {
        return readFile(Paths.get(path), encoding);
    }

    public static String readFile(Path path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(path);
        return new String(encoded, encoding);
    }

    public static String urlEncode(String path) {

        if (Objects.isNull(path)) {
            return null;
        }

        try {
            return StringUtils.replace(URLEncoder.encode(path, StandardCharsets.UTF_8.name()), "+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new PlatformFailureException("Input encoding not recognized", CoreExceptionIds.EX_DATA_INCORRECT_ENCODING);
        }
    }

    public static String urlDecode(String path) {

        if (Objects.isNull(path)) {
            return null;
        }

        try {
            return URLDecoder.decode(path, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new PlatformFailureException("Input encoding not recognized", CoreExceptionIds.EX_DATA_INCORRECT_ENCODING);
        }
    }

    /**
     * Save file temp folder.
     *
     * @param attachment the attachment
     * @return the java.nio.file. path
     */
    public static Path saveFileTempFolder(Attachment attachment) {

        String fileName = String.join(DELIMETER, SDF.format(new Date()),
                attachment.getContentDisposition().getParameter(FILENAME));

        String tempDirectory = getImportDirectory();
        try {
            Files.createDirectories(Paths.get(tempDirectory));
            Path path = Paths.get(String.join(File.separator, tempDirectory, fileName));
            Files.deleteIfExists(path);
            InputStream in = attachment.getObject(InputStream.class);
            Files.copy(in, path);
            return path;
        } catch (IOException ex) {
            throw new PlatformFailureException("Exception occurs while saving file.",
                    CoreExceptionIds.EX_DATA_SAVE_FILE, ex);
        }
    }

    /**
     * Gets temp import directory.
     *
     * @return string path
     */
    public static String getImportDirectory() {
        return String.join(File.separator, System.getProperty(CATALINA_BASE), TEMP, TO_IMPORT);
    }

    /**
     * Gets temp export directory.
     *
     * @return string path
     */
    public static String getExportDirectory() {
        return String.join(File.separator, System.getProperty(CATALINA_BASE), TEMP, TO_EXPORT);
    }
}
