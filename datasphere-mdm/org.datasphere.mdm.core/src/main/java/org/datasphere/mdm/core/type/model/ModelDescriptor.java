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
package org.datasphere.mdm.core.type.model;

import java.util.Objects;

/**
 * @author Mikhail Mikhailov on Oct 8, 2020
 * The model descriptor.
 */
public final class ModelDescriptor<I extends ModelInstance<?>> {
    /**
     * ID/name.
     */
    private final String typeId;
    /**
     * Instance implementing class.
     */
    private final Class<I> instance;
    /**
     * Constructor.
     * @param typeId the model type name/ID
     */
    public ModelDescriptor(String typeId, Class<I> instance) {
        super();
        Objects.requireNonNull(typeId, "Model type id must not be null.");
        this.typeId = typeId;
        this.instance = instance;
    }
    /**
     * @return the name
     */
    public String getModelTypeId() {
        return typeId;
    }
    /**
     * @return the instance
     */
    public Class<I> getInstance() {
        return instance;
    }
    /**
     * Shortcut.
     * @param <I> instance type
     * @param name the model type id
     * @param klass the class
     * @return descriptor
     */
    public static<I extends ModelInstance<?>> ModelDescriptor<I> of(String name, Class<I> klass) {
        return new ModelDescriptor<>(name, klass);
    }
}
