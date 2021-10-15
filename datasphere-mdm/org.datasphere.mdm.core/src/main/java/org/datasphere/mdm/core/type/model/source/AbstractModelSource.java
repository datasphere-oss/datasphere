package org.datasphere.mdm.core.type.model.source;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * @author Mikhail Mikhailov on Oct 30, 2020
 */
public abstract class AbstractModelSource<X extends AbstractModelSource<X>> extends VersionedObject<X> implements Serializable {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = -7626604523754261598L;
    /**
     * This model name.
     */
    @JacksonXmlProperty(isAttribute = true)
    protected String name;
    /**
     * This model display name.
     */
    @JacksonXmlProperty(isAttribute = true)
    protected String displayName;
    /**
     * This model description.
     */
    @JacksonXmlProperty(isAttribute = true)
    protected String description;
    /**
     * The storage id.
     */
    @JacksonXmlProperty(isAttribute = true)
    protected String storageId;
    /**
     * Created by.
     */
    @JacksonXmlProperty(isAttribute = true)
    protected String createdBy;
    /**
     * Create date.
     */
    @JacksonXmlProperty(isAttribute = true)
    protected OffsetDateTime createDate;
    /**
     * The custom properties.
     */
    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<CustomProperty> customProperties;
    /**
     * Constructor.
     */
    protected AbstractModelSource() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String value) {
        this.storageId = value;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(OffsetDateTime value) {
        this.createDate = value;
    }

    public List<CustomProperty> getCustomProperties() {
        if (customProperties == null) {
            customProperties = new ArrayList<>();
        }
        return customProperties;
    }

    public void setCustomProperties(List<CustomProperty> customProperties) {
        withCustomProperties(customProperties);
    }

    public X withName(String value) {
        setName(value);
        return self();
    }

    public X withDisplayName(String value) {
        setDisplayName(value);
        return self();
    }

    public X withDescription(String value) {
        setDescription(value);
        return self();
    }

    public X withStorageId(String value) {
        setStorageId(value);
        return self();
    }

    public X withCreatedBy(String value) {
        setCreatedBy(value);
        return self();
    }

    public X withCreateDate(OffsetDateTime value) {
        setCreateDate(value);
        return self();
    }

    public X withCustomProperties(CustomProperty... values) {
        if (ArrayUtils.isNotEmpty(values)) {
            Collections.addAll(getCustomProperties(), values);
        }
        return self();
    }

    public X withCustomProperties(Collection<CustomProperty> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            getCustomProperties().addAll(values);
        }
        return self();
    }
}
