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

package org.datasphere.mdm.core.type.keys;

import java.io.Serializable;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mikhail Mikhailov
 * Unattended external id.
 */
public class ExternalId implements Serializable {
    /**
     * The field separator.
     */
    public static final char FS = '\u001F';
    /**
     * SVUID.
     */
    private static final long serialVersionUID = 6198988620815406343L;
    /**
     * The external id.
     */
    private final String id;
    /**
     * The entity name.
     */
    private final String entityName;
    /**
     * The source system.
     */
    private final String sourceSystem;
    /**
     * Compact representation of the key.
     */
    private String compacted;
    /**
     * Constructor.
     */
    private ExternalId(String externalId, String entityName, String sourceSystem) {
        super();
        this.id = externalId;
        this.entityName = entityName;
        this.sourceSystem = sourceSystem;
    }
    /**
     * @return the externalId
     */
    public String getId() {
        return id;
    }
    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }
    /**
     * @return the sourceSystem
     */
    public String getSourceSystem() {
        return sourceSystem;
    }
    /**
     * Validity check.
     * @return whether this key is valid.
     */
    public boolean isValid() {
        return StringUtils.isNotBlank(this.id) && StringUtils.isNotBlank(this.entityName) && StringUtils.isNotBlank(this.sourceSystem);
    }
    /**
     * Compacts itself.
     * @return compacted view.
     */
    public String compact() {

        if (compacted == null) {
            compacted = ExternalId.compact(this);
        }

        return compacted;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, sourceSystem);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof ExternalId)) {
            return false;
        }

        ExternalId other = (ExternalId) obj;
        return Objects.equals(this.id, other.id)
            && Objects.equals(this.entityName, other.entityName)
            && Objects.equals(this.sourceSystem, other.sourceSystem);
    }
    /**
     * Creates an instance.
     * @param externalId the ext id string
     * @param entityName the entity name string
     * @param sourceSystem  the source system string
     * @return alias key
     */
    public static ExternalId of(String externalId, String entityName, String sourceSystem) {
        return new ExternalId(externalId, entityName, sourceSystem);
    }
    /**
     * Creates an instance.
     * @param spec the string to parse in form ext_id<separator char>entity_name<separator char>source_system
     * @param separator the separator
     * @return alias key
     */
    public static ExternalId of(String spec, char separator) {

        String[] parts = StringUtils.split(spec, separator);
        if (parts == null || parts.length != 3) {
            return null;
        }

        return new ExternalId(StringUtils.trim(parts[0]), StringUtils.trim(parts[1]), StringUtils.trim(parts[2]));
    }
    /**
     * Compacts external id fields, using system separator.
     * @param externalId the id
     * @param entityName the entity name
     * @param sourceSystem the source system
     * @return compact representation
     */
    public static String compact(String externalId, String entityName, String sourceSystem) {

        final StringBuilder buf = new StringBuilder(3 * 16);
        for (int i = 0; i < 3; i++) {

            switch (i) {
            case 0:
                buf.append(externalId);
                break;
            case 1:
                buf.append(entityName);
                break;
            case 2:
                buf.append(sourceSystem);
                break;
            default:
                // Can't really happen, but makes sonar happy
                break;
            }

            if (i < 2) {
                buf.append(FS);
            }
        }

        return buf.toString();
    }
    /**
     * Same as above but using an object.
     * @param value the object
     * @return compact representation
     */
    public static String compact(ExternalId value) {
        return compact(value.getId(), value.getEntityName(), value.getSourceSystem());
    }
    /**
     * Expands compact representation into a new object
     * @param value the compact value
     * @return object
     */
    public static ExternalId uncompact(String value) {
        return of(value, FS);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return new StringBuilder()
                .append("id = ").append(id)
                .append(", entityName = ").append(entityName)
                .append(", sourceSystem = ").append(sourceSystem)
                .toString();
    }
}
