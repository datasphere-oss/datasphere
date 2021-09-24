package com.huahui.datasphere.mdm.core.configuration;

import com.huahui.datasphere.mdm.system.type.messaging.DomainType;
import com.huahui.datasphere.mdm.system.util.ResourceUtils;

import com.huahui.datasphere.mdm.core.module.CoreModule;
import com.huahui.datasphere.mdm.core.type.messaging.CoreTypes;

/**
 * Core messaging domain(s).
 * @author theseusyang on Jul 15, 2020
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
