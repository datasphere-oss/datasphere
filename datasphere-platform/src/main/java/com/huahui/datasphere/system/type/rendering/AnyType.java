package com.huahui.datasphere.system.type.rendering;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Any value type
 *
 * @author Alexandr Serov
 * @since 17.09.2020
 **/
public class AnyType {

    private static final AnyType[] EMPTY_PARAMETERS = new AnyType[0];

    /**
     * Common java types as any type cache
     */
    private static final Map<Class<?>, AnyType> cached = Collections.unmodifiableMap(Arrays.stream(new Class<?>[]{
        Boolean.class, Boolean[].class, Byte.class, Byte[].class, Character.class, Character[].class,
        Short.class, Short[].class, Integer.class, Integer[].class, Float.class, Float[].class,
        Long.class, Long[].class, Double.class, Double[].class,
        String.class, String[].class,
        LocalDate.class, LocalDate[].class, LocalDateTime.class, LocalDateTime[].class
    }).collect(Collectors.toMap(Function.identity(), AnyType::new)));

    /**
     * Base (raw) type
     */
    private final Class<?> javaClass;

    /**
     * Generic type parameters
     */
    private final AnyType[] parameters;

    /**
     * Generic parameters count
     */
    private final int paramCount;

    /**
     * Is array type
     */
    private final boolean array;

    public AnyType(Class<?> cls, AnyType[] parameters) {
        this.parameters = parameters;
        this.paramCount = parameters.length;
        array = cls.isArray();
        javaClass = array ? cls.getComponentType() : cls;
    }

    public AnyType(Class<?> cls) {
        this(cls, EMPTY_PARAMETERS);
    }

    public static AnyType anyType(Class<?> javaClass) {
        AnyType result = cached.get(javaClass);
        if (result == null) {
            result = new AnyType(javaClass);
        }
        return result;
    }

    public static AnyType anyType(Class<?> javaClass, AnyType[] parameters) {
        return parameters.length == 0 ? anyType(javaClass) : new AnyType(javaClass, parameters);
    }

    /**
     * Get generic parameter by index
     *
     * @param paramIndex parameter index
     * @return return parameter
     */
    public AnyType getParameter(int paramIndex) {
        if (paramIndex >= 0 && paramIndex < paramCount) {
            return parameters[paramIndex];
        } else {
            throw new IllegalArgumentException("Illegal parameter index: " + paramIndex + ", expected [0:" + paramCount + "]");
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(javaClass.getSimpleName());
        if (paramCount > 0) {
            builder.append('<').append(parameters[0]);
            for (int i = 1; i < paramCount; i++) {
                builder.append(',').append(parameters[i]);
            }
            builder.append('>');
        }
        if (array) {
            builder.append("[]");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnyType classInfo = (AnyType) o;
        return array == classInfo.array &&
            paramCount == classInfo.paramCount &&
            javaClass.equals(classInfo.javaClass) &&
            Arrays.equals(parameters, classInfo.parameters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(javaClass, array, paramCount);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }

    /**
     * @return base java class (raw type)
     */
    public Class<?> getJavaClass() {
        return javaClass;
    }

    /**
     * @return true if is array Target
     */
    public boolean isArray() {
        return array;
    }

    /**
     * @return generic parameter count
     */
    public int getParamCount() {
        return paramCount;
    }
}
