package org.datasphere.mdm.core.configuration;

import org.datasphere.mdm.core.module.CoreModule;
import org.datasphere.mdm.core.type.messaging.CoreTypes;
import org.datasphere.mdm.system.type.messaging.DomainType;
import org.datasphere.mdm.system.util.ResourceUtils;

/**
 * Core messaging domain(s).
 * @author Mikhail Mikhailov on Jul 15, 2020
 */
public class CoreMessagingDomain {
    /**
     * Constructor.
     */
    private CoreMessagingDomain() {
        super();
    }
    /**
     * Domain name.
     */
    public static final String NAME = "core";
    /**
     * The sole core domain.
     */
    public static final DomainType DOMAIN =
            DomainType.ofLocalized(NAME, ResourceUtils.asString("classpath:/routes/core.xml"))
                .withDescription(CoreModule.MODULE_ID + ".messaging.domain.description")
                .withMessageTypes(CoreTypes.TYPES);
}
