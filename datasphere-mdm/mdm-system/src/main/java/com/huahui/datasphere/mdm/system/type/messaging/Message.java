package com.huahui.datasphere.mdm.system.type.messaging;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author theseusyang on Jul 13, 2020
 */
public class Message {
    /**
     * The content.
     */
    private Object body;
    /**
     * Headers.
     */
    private Map<Header, Object> headers;
    /**
     * Throwable failure cause.
     */
    private Throwable cause;
    /**
     * The type.
     */
    private final MessageType type;
    /**
     * Constructor.
     */
    public Message(MessageType type) {
        super();

        Objects.requireNonNull(type, "Message type must not be null.");
        this.type = type;
    }
    /**
     * @return the body
     */
    public Object getBody() {
        return body;
    }
    /**
     * @return the cause
     */
    public Throwable getCause() {
        return cause;
    }
    /**
     * @return the headers
     */
    public Map<Header, Object> getHeaders() {
        return Objects.isNull(headers) ? Collections.emptyMap() : headers;
    }
    /**
     * @return the type
     */
    public MessageType getType() {
        return type;
    }
    /**
     * Sets the body.
     * @param body the body to set
     * @return self
     */
    public Message withBody(Object body) {
        this.body = body;
        return this;
    }
    /**
     * Sets a possible failure cause.
     * @param th the failure cause
     * @return self
     */
    public Message withCause(Throwable th) {
        this.cause = th;
        return this;
    }
    /**
     * @param headers the headers to set
     */
    public Message withHeader(Header h, Object v) {
        return withHeaders(Collections.singletonMap(h, v));
    }
    /**
     * @param headers the headers to set
     */
    public Message withHeaders(Map<Header, Object> headers) {

        if (Objects.isNull(this.headers)) {
            this.headers = new HashMap<>();
        }

        this.headers.putAll(headers);
        return this;
    }
}
