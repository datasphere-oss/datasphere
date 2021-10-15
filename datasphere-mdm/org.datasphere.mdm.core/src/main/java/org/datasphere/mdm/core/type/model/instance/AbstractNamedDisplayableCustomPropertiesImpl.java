package org.datasphere.mdm.core.type.model.instance;

import java.util.Collection;

import org.datasphere.mdm.core.type.model.NamedDisplayableElement;
import org.datasphere.mdm.core.type.model.source.CustomProperty;

/**
 * @author Mikhail Mikhailov on Oct 16, 2020
 * A combination of custom props and ND, what is the case for most top level objects.
 */
public abstract class AbstractNamedDisplayableCustomPropertiesImpl extends AbstractCustomPropertiesImpl implements NamedDisplayableElement {
    /**
     * Name.
     */
    private final String name;
    /**
     * Display name.
     */
    private final String displayName;
    /**
     * The description.
     */
    private final String description;
    /**
     * Constructor.
     * @param properties
     */
    protected AbstractNamedDisplayableCustomPropertiesImpl(String name, String displayName, Collection<CustomProperty> properties) {
        super(properties);
        this.name = name;
        this.displayName = displayName;
        this.description = null;
    }
    /**
     * Constructor.
     * @param properties
     */
    protected AbstractNamedDisplayableCustomPropertiesImpl(String name, String displayName, String description, Collection<CustomProperty> properties) {
        super(properties);
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }
}
