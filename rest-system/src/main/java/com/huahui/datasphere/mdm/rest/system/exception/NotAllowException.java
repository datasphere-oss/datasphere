package com.huahui.datasphere.mdm.rest.system.exception;

import com.huahui.datasphere.mdm.system.exception.DomainId;
import com.huahui.datasphere.mdm.system.exception.PlatformRuntimeException;

/**
 * Not allow exception
 *
 * @author theseusyang
 * @since 09.12.2020
 **/
public class NotAllowException extends PlatformRuntimeException {

    private static final String DEFAULT_ERROR_MESSAGE = "operation is not allowed";

    /**
     * This exception domain.
     */
    private static final DomainId NOT_ALLOW_EXCEPTION = () -> "NOT_ALLOW_EXCEPTION";

    public NotAllowException(String message, Object... args) {
        super(message, SystemRestExceptionIds.EX_NOT_ALLOW, args);
    }

    public NotAllowException() {
        this(DEFAULT_ERROR_MESSAGE);
    }

    @Override
    public DomainId getDomain() {
        return NOT_ALLOW_EXCEPTION;
    }
}
