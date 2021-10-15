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

package org.datasphere.mdm.core.dto;

import java.util.Date;

/**
 * The Class UserWithPasswordRO.
 * @author ilya.bykov
 */
public class UserWithPasswordDTO extends UserDTO {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -5010154347779875267L;
    
    /** The password. */
    private String password;
    
    /** The password last changed at. */
    private Date passwordLastChangedAt;
    
    /** The password updated by. */
    private String passwordUpdatedBy;

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

	/**
	 * Gets the password last changed at.
	 *
	 * @return the passwordLastChangedAt
	 */
	public Date getPasswordLastChangedAt() {
		return passwordLastChangedAt;
	}

	/**
	 * Sets the password last changed at.
	 *
	 * @param passwordLastChangedAt
	 *            the passwordLastChangedAt to set
	 */
	public void setPasswordLastChangedAt(Date passwordLastChangedAt) {
		this.passwordLastChangedAt = passwordLastChangedAt;
	}

	/**
	 * Gets the password updated by.
	 *
	 * @return the passwordUpdatedBy
	 */
	public String getPasswordUpdatedBy() {
		return passwordUpdatedBy;
	}

	/**
	 * Sets the password updated by.
	 *
	 * @param passwordUpdatedBy
	 *            the passwordUpdatedBy to set
	 */
	public void setPasswordUpdatedBy(String passwordUpdatedBy) {
		this.passwordUpdatedBy = passwordUpdatedBy;
	}
}
