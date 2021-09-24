package com.huahui.datasphere.mdm.system.type.rendering;

import java.util.List;
import java.util.Objects;

/**
 * Defining a fragment of an object
 *
 * @author Alexandr Serov
 * @since 09.10.2020
 **/
public class FragmentDef {

    public static final String CURRENT_VERSION = "current";

    public static final String PAYLOAD_FIELD_NAME = "payload";

    /**
     * Target input/output object type
     */
    private final Class<?> objectType;

    /**
     * Fragment fields
     */
    private final List<FieldDef> fields;

    /**
     * Target  input/output object field
     */
    private final String objectField;

    /**
     * Fragment version
     */
    private final String version;

    public FragmentDef(Class<?> objectType, String objectField, List<FieldDef> fields, String version) {
        this.objectType = objectType;
        this.objectField = objectField;
        this.fields = fields;
        this.version = version;
    }

    public static FragmentDef fragmentDef(Class<?> objectType, List<FieldDef> fields) {
        return new FragmentDef(objectType, PAYLOAD_FIELD_NAME, fields, CURRENT_VERSION);
    }

    public static FragmentDef fragmentDef(Class<?> objectType, String payloadFieldName, List<FieldDef> fields) {
        return new FragmentDef(objectType, payloadFieldName, fields, CURRENT_VERSION);
    }


    public static FragmentDef fragmentDef(Class<?> objectType, List<FieldDef> fields, String version) {
        return new FragmentDef(objectType, PAYLOAD_FIELD_NAME, fields, version);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(objectType.getSimpleName())
            .append("{\n").append(objectField);
        for (FieldDef field : fields) {
            builder.append("\n\t").append(field.getDescription());
            builder.append("\n\t").append(field.getFieldType()).append(' ').append(field.getFieldName());
        }
        return builder.append('}').toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FragmentDef that = (FragmentDef) o;
        return Objects.equals(objectType, that.objectType) &&
            Objects.equals(fields, that.fields) &&
            Objects.equals(objectField, that.objectField) &&
            Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectType, fields, objectField, version);
    }

    /**
     * @return target object type
     */
    public Class<?> getObjectType() {
        return objectType;
    }

    /**
     * @return fragment fields
     */
    public List<FieldDef> getFields() {
        return fields;
    }

    /**
     * @return target object field name
     */
    public String getObjectField() {
        return objectField;
    }

    /**
     * @return fragment version
     */
    public String getVersion() {
        return version;
    }
}
