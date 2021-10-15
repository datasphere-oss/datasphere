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

/**
 * Date: 05.07.2016
 */

package org.datasphere.mdm.core.dto;

import java.io.Serializable;

import org.datasphere.mdm.core.type.security.CustomProperty;

/**
 * FIXDOC: add file description.
 *
 * @author amagdenko
 */
public class RolePropertyDTO implements CustomProperty, Serializable {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -6203608704528641376L;

    /** Identifier. */
    private Long id;

    /** The name. */
   	private String name;
   	/**
     * Is required.
     */
    private boolean required;

    /**
     * The display name.
     */
    private String displayName;

    /**
     * The value.
     */
    private String value;

    /**
     * UN-12188
     */
    private boolean readOnly;

    /**
     * the type of control defined by integration developers
     */
    private String fieldType;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Checks if is required.
     *
     * @return true, if is required
     */
    public boolean isRequired() {
		return required;
	}

	/**
	 * Sets the required.
	 *
	 * @param required the new required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/* (non-Javadoc)
	 * @see com.unidata.mdm.backend.common.integration.auth.CustomProperty#getName()
	 */
	@Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.common.integration.auth.CustomProperty#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName the new display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /* (non-Javadoc)
     * @see com.unidata.mdm.backend.common.integration.auth.CustomProperty#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Gets the type of control defined by integration developers
     *
     * @return value
     */
    @Override
    public String getFieldType() {
        return fieldType;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    public RolePropertyDTO setFieldType(String fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public RolePropertyDTO setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }
}
