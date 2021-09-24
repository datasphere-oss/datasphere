package com.huahui.datasphere.mdm.system.type.messaging;

import com.huahui.datasphere.mdm.system.type.messaging.Header.HeaderType;

/**
 * System headers, which are present in all types of messages.
 * @author theseusyang on Jul 10, 2020
 */
public class SystemHeaders {
    /**
     * Disabling ctor.
     * Constructor.
     */
    private SystemHeaders() {
        super();
    }
    /**
     * The user, which account sent the message.
     */
    public static final Header LOGIN = new Header("login", HeaderType.STRING);
    /**
     * Throwable, wich possibly caused the message.
     */
    public static final Header THROWABLE = new Header("throwable", HeaderType.OBJECT);
    /**
     * General operation 'success' flag, marking messages containing exceptions or errors automatically.
     */
    public static final Header SUCCESS = new Header("success", HeaderType.BOOLEAN);
    /**
     * The client IP.
     */
    public static final Header CLIENT_IP = new Header("client_ip", HeaderType.STRING);
    /**
     * The server IP.
     */
    public static final Header SERVER_IP = new Header("server_ip", HeaderType.STRING);
    /**
     * The causing endpoint.
     */
    public static final Header ENDPOINT = new Header("endpoint", HeaderType.STRING);
    /**
     * Timestampt.
     */
    public static final Header WHEN_HAPPENED = new Header("when_happened", HeaderType.INSTANT);
    /**
     * Domain name.
     */
    public static final Header DOMAIN = new Header("domain", HeaderType.STRING);
    /**
     * Type name.
     */
    public static final Header TYPE = new Header("type", HeaderType.STRING);
    /**
     * All headers.
     */
    private static final Header[] HEADERS = new Header[] {
            LOGIN,
            THROWABLE,
            SUCCESS,
            CLIENT_IP,
            SERVER_IP,
            ENDPOINT,
            WHEN_HAPPENED,
            DOMAIN,
            TYPE
    };

    public static Header[] all() {
        return HEADERS;
    }
}
