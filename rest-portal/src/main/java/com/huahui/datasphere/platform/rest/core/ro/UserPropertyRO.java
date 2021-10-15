/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.ro;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class UserProperties.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPropertyRO {
    private Long id;
    /**
     * is required
     */
    private boolean required;
    /**
     * The name.
     */
    private String name;

    /**
     * The display name.
     */
    private String displayName;

    /**
     * The value.
     */
    private String value;

    private boolean readOnly;

    /**
     * type of ui control defined by integration developers.
     */
    private String fieldType;

    /**
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Is required.
     * @return is required.
     */
    public boolean isRequired() {
		return required;
	}
    /**
     * Set required.
     * @param required
     */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName
     *            the new display name
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String getFieldType() {
        return fieldType;
    }

    public UserPropertyRO setFieldType(String fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public UserPropertyRO setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }
}
