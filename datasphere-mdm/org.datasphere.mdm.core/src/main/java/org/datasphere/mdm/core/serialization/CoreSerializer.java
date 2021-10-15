package org.datasphere.mdm.core.serialization;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.ArrayUtils;
import org.datasphere.mdm.core.serialization.protostuff.CoreSchemas;
import org.datasphere.mdm.core.type.data.DataRecord;
import org.datasphere.mdm.core.type.data.impl.SerializableDataRecord;
import org.datasphere.mdm.core.type.formless.BundlesArray;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;

/**
 * @author Mikhail Mikhailov on Sep 14, 2020
 */
public class CoreSerializer {
    /**
     * Constructor.
     */
    private CoreSerializer() {
        super();
    }
    /**
     * Dumps to Protostuff
     * @param record the record
     * @return byte array (empty on null or empty input)
     */
    @Nonnull
    public static byte[] recordToProtostuff(DataRecord record) {

        if (record == null || record.isEmpty()) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        return ProtostuffIOUtil.toByteArray(record, CoreSchemas.DATA_RECORD_SCHEMA, LinkedBuffer.allocate());
    }
    /**
     * Restores from Protostuff
     * @param buf the buffer to restore.
     * @return record
     */
    public static DataRecord recordFromProtostuff(byte[] buf) {

        if (ArrayUtils.isNotEmpty(buf)) {

            SerializableDataRecord record = new SerializableDataRecord();
            ProtostuffIOUtil.mergeFrom(buf, record, CoreSchemas.DATA_RECORD_SCHEMA);
            return record;
        }

        return null;
    }
    /**
     * Dumps to Protostuff
     * @param array the record
     * @return byte array (empty on null or empty input)
     */
    @Nonnull
    public static byte[] bundlesArrayToProtostuff(BundlesArray array) {

        if (array == null || array.isEmpty()) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        return ProtostuffIOUtil.toByteArray(array, CoreSchemas.BUNDLES_ARRAY_SCHEMA, LinkedBuffer.allocate());
    }
    /**
     * Restores from Protostuff
     * @param buf the buffer to restore.
     * @return record
     */
    public static BundlesArray bundlesArrayFromProtostuff(byte[] buf) {

        if (ArrayUtils.isNotEmpty(buf)) {

            BundlesArray record = new BundlesArray();
            ProtostuffIOUtil.mergeFrom(buf, record, CoreSchemas.BUNDLES_ARRAY_SCHEMA);
            return record;
        }

        return null;
    }
}
