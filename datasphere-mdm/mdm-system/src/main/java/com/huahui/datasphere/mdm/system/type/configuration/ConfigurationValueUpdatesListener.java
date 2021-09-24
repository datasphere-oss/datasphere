package com.huahui.datasphere.mdm.system.type.configuration;

/**
 * Listens for updates of a particular property.
 * @author theseusyang on Apr 21, 2020
 */
public interface ConfigurationValueUpdatesListener {
    /**
     * This method is invoked, when a property value gets updated.
     * The property definition must be constructed with readOnly == false to be updatable.
     * The value itself has to be injected by:
     *
     * {@literal @}ConfigurationRef
     * private ConfigurationValue value;
     *
     * The listener itself has to be implemented by the <strong>enclosing class</strong>.
     * @param property the property definition
     */
    void configurationValueUpdated(ConfigurationProperty<?> property);
}
