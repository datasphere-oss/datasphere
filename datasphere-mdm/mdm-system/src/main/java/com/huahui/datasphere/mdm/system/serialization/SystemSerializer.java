package com.huahui.datasphere.mdm.system.serialization;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.ArrayUtils;

import com.huahui.datasphere.mdm.system.serialization.protostuff.SystemSchemas;
import com.huahui.datasphere.mdm.system.type.variables.Variables;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;

/**
 * @author theseusyang
 */
public class SystemSerializer {
    /**
     * Constructor.
     */
    private SystemSerializer() {
        super();
    }
    /**
     * Dumps {@link Variables} object to protostuff.
     * @param v the variables object
     * @return byte array (empty on null or empty input)
     */
    @Nonnull
    public static byte[] variablesToProtostuff(Variables v) {

        if (v == null || v.isEmpty()) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        return ProtostuffIOUtil.toByteArray(v, SystemSchemas.VARIABLES_SCHEMA, LinkedBuffer.allocate());
    }
    /**
     * Reads variables from PSTF buffer.
     * @param buf the buffer
     * @return {@link Variables} object or null
     */
    @Nullable
    public static Variables variablesFromProtostuff(byte[] buf) {

        if (ArrayUtils.isNotEmpty(buf)) {

            Variables v = new Variables();
            ProtostuffIOUtil.mergeFrom(buf, v, SystemSchemas.VARIABLES_SCHEMA);
            return v;
        }

        return null;
    }
}
