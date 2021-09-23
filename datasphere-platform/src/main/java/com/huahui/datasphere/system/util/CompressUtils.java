package com.huahui.datasphere.system.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Mikhail Mikhailov on Oct 8, 2020
 * Simple byte array in memory compression.
 */
public final class CompressUtils {
    /**
     * Constructor.
     */
    private CompressUtils() {
        super();
    }

    public static byte[] inflate(byte[] input) throws DataFormatException {
        return inflate(input, false);
    }

    public static byte[] inflate(byte[] input, boolean nowrap) throws DataFormatException {

        Inflater inflater = new Inflater(nowrap);
        inflater.setInput(input);

        byte[] buffer = new byte[input.length];
        int inflateLen;

        ByteArrayOutputStream inflated = new ByteArrayOutputStream();
        while (!inflater.finished()) {

            inflateLen = inflater.inflate(buffer, 0, input.length);
            if (inflateLen == 0 && !inflater.finished()) {
                if (inflater.needsInput()) {
                    throw new DataFormatException("Inflater can not inflate all the token bytes");
                }
                break;
            }

            inflated.write(buffer, 0, inflateLen);
        }

        return inflated.toByteArray();
    }

    public static byte[] unzip(byte[] input, String entryName) throws IOException {

        if (ArrayUtils.isEmpty(input)) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(input);
        ZipInputStream zis = new ZipInputStream(bis, StandardCharsets.UTF_8);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ZipEntry ze;
        byte[] buf = new byte[2048];
        while ((ze = zis.getNextEntry()) != null) {

            if (!StringUtils.equals(ze.getName(), entryName)) {
                zis.closeEntry();
                continue;
            }

            int f;
            while((f = zis.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, f);
            }

            zis.closeEntry();
            break;
        }

        zis.close();
        return bos.toByteArray();
    }

    public static byte[] deflate(byte[] input) {
        return deflate(input, false);
    }

    public static byte[] deflate(byte[] input, boolean nowrap) {
        return deflate(input, Deflater.DEFLATED, nowrap);
    }

    public static byte[] deflate(byte[] input, int level, boolean nowrap) {

        Deflater compresser = new Deflater(level, nowrap);

        compresser.setInput(input);
        compresser.finish();

        byte[] output = new byte[input.length * 2];

        int targetLength = compresser.deflate(output);

        byte[] result = new byte[targetLength];
        System.arraycopy(output, 0, result, 0, targetLength);

        return result;
    }

    public static byte[] zip(byte[] input, String entryName) throws IOException {

        if (ArrayUtils.isEmpty(input)) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(bos, StandardCharsets.UTF_8);

        zos.putNextEntry(new ZipEntry(entryName));
        zos.write(input, 0, input.length);
        zos.closeEntry();
        zos.close();

        return bos.toByteArray();
    }
}
