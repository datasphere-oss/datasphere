package org.datasphere.mdm.core.type.model.instance;

import org.datasphere.mdm.core.type.model.NamedDisplayableElement;

/**
 * @author Mikhail Mikhailov on Oct 7, 2020
 * Just name, displayName holder.
 */
public abstract class AbstractNamedDisplayableImpl implements NamedDisplayableElement {
    /**
     * Name.
     */
    private final String name;
    /**
     * Display name.
     */
    private final String displayName;
    /**
     * Display name.
     */
    private final String description;
    /**
     * Constructor.
     */
    protected AbstractNamedDisplayableImpl(String name, String displayName) {
        super();
        this.name = name;
        this.displayName = displayName;
        this.description = null;
    }
    /**
     * Constructor.
     */
    protected AbstractNamedDisplayableImpl(String name, String displayName, String description) {
        super();
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
