package org.datasphere.mdm.core.type.messaging;

import org.datasphere.mdm.core.module.CoreModule;
import org.datasphere.mdm.system.type.messaging.MessageType;

/**
 * Default core types.
 * @author Mikhail Mikhailov on Jul 10, 2020
 */
public class CoreTypes {
    /**
     * Constructor.
     */
    private CoreTypes() {
        super();
    }
    /**
     * Login audit message.
     */
    public static final MessageType LOGIN = new MessageType("login")
            .withSubsystem(CoreSubsystems.AUDIT_SUBSYSTEM)
            .withDescription(CoreModule.MODULE_ID + ".messaging.login")
            .withHeaders(CoreHeaders.ACTION_REASON);
    /**
     * Logout audit message.
     */
    public static final MessageType LOGOUT = new MessageType("logout")
            .withSubsystem(CoreSubsystems.AUDIT_SUBSYSTEM)
            .withDescription(CoreModule.MODULE_ID + ".messaging.logout")
            .withHeaders(CoreHeaders.ACTION_REASON);
    /**
     * Role create audit message.
     */
    public static final MessageType ROLE_CREATE = new MessageType("role_create")
            .withSubsystem(CoreSubsystems.AUDIT_SUBSYSTEM)
            .withDescription(CoreModule.MODULE_ID + ".messaging.role_create")
            .withHeaders(CoreHeaders.ROLE_NAME);
    /**
     * Role delete audit message.
     */
    public static final MessageType ROLE_DELETE = new MessageType("role_delete")
            .withSubsystem(CoreSubsystems.AUDIT_SUBSYSTEM)
            .withDescription(CoreModule.MODULE_ID + ".messaging.role_delete")
            .withHeaders(CoreHeaders.ROLE_NAME);
    /**
     * Role update audit message.
     */
    public static final MessageType ROLE_UPDATE = new MessageType("role_update")
            .withSubsystem(CoreSubsystems.AUDIT_SUBSYSTEM)
            .withDescription(CoreModule.MODULE_ID + ".messaging.role_update")
            .withHeaders(CoreHeaders.ROLE_NAME);
    /**
     * Role label attach audit message.
     */
    public static final MessageType ROLE_LABEL_ATTACH = new MessageType("role_label_attach")
            .withSubsystem(CoreSubsystems.AUDIT_SUBSYSTEM)
            .withDescription(CoreModule.MODULE_ID + ".messaging.role_label_attach")
            .withHeaders(CoreHeaders.ROLE_NAME, CoreHeaders.LABEL_NAME);
    /**
     * Label create audit message.
     */
    public static final MessageType LABEL_CREATE = new MessageType("label_create")
            .withSubsystem(CoreSubsystems.AUDIT_SUBSYSTEM)
            .withDescription(CoreModule.MODULE_ID + ".messaging.label_create")
            .withHeaders(CoreHeaders.LABEL_NAME);
    /**
     * Label delete audit message.
     */
    public static final MessageType LABEL_DELETE = new MessageType("label_delete")
            .withSubsystem(CoreSubsystems.AUDIT_SUBSYSTEM)
            .withDescription(CoreModule.MODULE_ID + ".messaging.label_delete")
            .withHeaders(CoreHeaders.LABEL_NAME);
    /**
     * Label update audit message.
     */
    public static final MessageType LABEL_UPDATE = new MessageType("label_update")
            .withSubsystem(CoreSubsystems.AUDIT_SUBSYSTEM)
            .withDescription(CoreModule.MODULE_ID + ".messaging.label_update")
            .withHeaders(CoreHeaders.LABEL_NAME);
    /**
     * All types as array.
     */
    public static final MessageType[] TYPES = {
            LOGIN,
            LOGOUT,
            ROLE_CREATE,
            ROLE_DELETE,
            ROLE_UPDATE,
            ROLE_LABEL_ATTACH,
            LABEL_CREATE,
            LABEL_DELETE,
            LABEL_UPDATE
    };
}
