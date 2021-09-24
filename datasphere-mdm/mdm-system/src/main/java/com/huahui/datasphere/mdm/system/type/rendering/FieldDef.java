package com.huahui.datasphere.mdm.system.type.rendering;

import java.util.Objects;

/**
 * Fragment Field Definition
 * Contains the name, type and description of the dynamic field
 *
 * @author Alexandr Serov
 * @since 09.10.2020
 **/
public class FieldDef {

    private static final String EMPTY_DESCRIPTION = "";

    private final String fieldName;
    private final String description;
    private final AnyType fieldType;

    public FieldDef(String fieldName, String description, AnyType fieldType) {
        this.fieldName = fieldName;
        this.description = description;
        this.fieldType = fieldType;
    }

    public static FieldDef fieldDef(String fieldName, String description, Class<?> fieldClass) {
        return new FieldDef(fieldName, description, AnyType.anyType(fieldClass));
    }

    public static FieldDef fieldDef(String fieldName, Class<?> fieldClass) {
        return new FieldDef(fieldName, EMPTY_DESCRIPTION, AnyType.anyType(fieldClass));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldDef fieldInfo = (FieldDef) o;
        return Objects.equals(fieldName, fieldInfo.fieldName) &&
            Objects.equals(description, fieldInfo.description) &&
            Objects.equals(fieldType, fieldInfo.fieldType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName, description, fieldType);
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getDescription() {
        return description;
    }

    public AnyType getFieldType() {
        return fieldType;
    }
}
