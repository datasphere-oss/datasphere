package com.huahui.datasphere.system.type.variables;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.huahui.datasphere.system.type.variables.Variable.Type;

/**
 * @author Mikhail Mikhailov on Sep 11, 2020
 * A simple cartridge for payload.
 */
public class Variables implements Iterable<Variable<?>> {
    /**
     * Variables storage.
     */
    private final Map<String, Variable<?>> payload = new HashMap<>();
    /**
     * Constructor.
     */
    public Variables() {
        super();
    }
    /**
     * Tells, whether a variable with the name is set.
     * @param name variable name
     * @return true if set, false otherwise
     */
    public boolean isSet(String name) {
        return payload.containsKey(name);
    }
    /**
     * Tells whether it is empty.
     * @return
     */
    public boolean isEmpty() {
        return payload.isEmpty();
    }
    /**
     * Map view.
     * @return map
     */
    public Map<String, Variable<?>> toMap() {
        return payload;
    }
    /**
     * Collection view.
     * @return collection
     */
    public Collection<Variable<?>> toCollection() {
        return payload.values();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Variable<?>> iterator() {
        return payload.values().iterator();
    }
    /**
     * Stream view.
     * @return stream
     */
    public Stream<Variable<?>> stream() {
        return payload.values().stream();
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, Integer value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, Long value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, Float value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, Double value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, Boolean value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, Enum<?> value) {
        return add(Variable.of(name, value == null ? null : value.name()));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, String value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, LocalDate value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, LocalTime value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, LocalDateTime value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null names are not accepted.
     * @param name the name
     * @param value the value
     * @return self
     */
    public Variables add(String name, Instant value) {
        return add(Variable.of(name, value));
    }
    /**
     * Adds a variable. Null arg and Null name are not accepted.
     * @param var the variable
     * @return self
     */
    public Variables add(Variable<?> var) {
        if (Objects.nonNull(var) && Objects.nonNull(var.getName())) {
            payload.put(var.getName(), var);
        }
        return this;
    }
    /**
     * Adds payload.
     * @param vars the payload
     * @return self
     */
    public Variables addAll(Map<String, Variable<?>> vars) {
        if (MapUtils.isNotEmpty(vars)) {
            return addAll(vars.values());
        }
        return this;
    }
    /**
     * Adds payload.
     * @param vars the payload
     * @return self
     */
    public Variables addAll(Collection<Variable<?>> vars) {
        if (CollectionUtils.isNotEmpty(vars)) {
            return addAll(vars.toArray(Variable[]::new));
        }
        return this;
    }
    /**
     * Adds payload.
     * @param vars the payload
     * @return self
     */
    public Variables addAll(Variable<?>... vars) {
        return addAll(Variable.asMap(vars));
    }
    /**
     * Gets a value by key.
     * @param <T> the expected type
     * @param name the variable name
     * @return value
     * @throws ClassCastException if T is not of the actual variable type
     */
    @SuppressWarnings("unchecked")
    public <T> T valueGet(String name) {
        Variable<?> v = payload.get(name);
        return v == null ? null : (T) v.getValue();
    }
    /**
     * Gets a value by key.
     * @param <T> the expected type
     * @param name the variable name
     * @return value
     * @throws ClassCastException if T is not of the actual variable type
     */
    public <T extends Enum<?>> T valueGet(String name, Class<T> klass) {

        Variable<?> v = payload.get(name);
        if (v == null || v.getType() != Type.STRING || !v.isSet()) {
            return null;
        }

        String val = v.getValue().toString();
        T[] fields = klass.getEnumConstants();
        for (T field : fields) {

            if (StringUtils.equalsIgnoreCase(field.name(), val)) {
                return field;
            }
        }

        return null;
    }
}
