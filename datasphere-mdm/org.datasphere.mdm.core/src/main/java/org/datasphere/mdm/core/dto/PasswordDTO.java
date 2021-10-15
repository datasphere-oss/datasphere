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

import java.time.LocalDateTime;

public class PasswordDTO extends BaseSecurityDTO {
    /**
     * SVUID.
     */
    private static final long serialVersionUID = -5612881974042201325L;

    private final UserDTO user;
    private final String hashedText;
    private final String text;
    private final LocalDateTime createAt;
    private final boolean isActive;

    public PasswordDTO(UserDTO user, String hashedText, boolean isActive, LocalDateTime createAt) {
        this.user = user;
        this.hashedText = hashedText;
        this.isActive = isActive;
        this.text = null;
        this.createAt = createAt;
    }

    public PasswordDTO(UserDTO user, String hashedText, String text, boolean isActive, LocalDateTime createAt) {
        this.user = user;
        this.hashedText = hashedText;
        this.text = text;
        this.isActive = isActive;
        this.createAt = createAt;
    }

    public UserDTO getUser() {
        return user;
    }

    public String getHashedText() {
        return hashedText;
    }

    public String getText() {
        return text;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
}
