package org.datasphere.mdm.core.type.model.source;

import java.io.Serializable;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
/**
 * @author Mikhail Mikhailov on Oct 6, 2020
 * A versioned object to support basic olock behavior upon upsert.
 */
public abstract class VersionedObject<X extends VersionedObject<X>> implements Serializable {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 4632726528028288191L;
    /**
     * The version.
     */
    @JacksonXmlProperty(isAttribute = true)
    protected Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer value) {
        this.version = value;
    }

    public X withVersion(Integer value) {
        setVersion(value);
        return self();
    }

    @SuppressWarnings("unchecked")
    protected X self() {
        return (X) this;
    }
}
