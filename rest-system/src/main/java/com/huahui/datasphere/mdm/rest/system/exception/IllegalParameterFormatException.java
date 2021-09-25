package com.huahui.datasphere.mdm.rest.system.exception;

import java.util.Collections;

import com.huahui.datasphere.mdm.system.exception.ExceptionId;
import com.huahui.datasphere.mdm.system.exception.PlatformValidationException;
import com.huahui.datasphere.mdm.system.exception.ValidationResult;

/**
 * An exception indicating an incorrectly specified parameter.
 * For example, the timestamp does not match the required format
 *
 * @author theseusyang
 * @since 30.10.2020
 **/
public class IllegalParameterFormatException extends PlatformValidationException {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = 7511277332714136954L;

    public static final ExceptionId EX_ILLEGAL_PARAMETER_FORMAT = new ExceptionId("ILLEGAL_PARAMETER_FORMAT", "app.rest.illegalParameterFormat");

    public static final String ILLEGAL_PARAMETER_FORMAT_ERROR_MESSAGE = "The parameter value does not match the required format.";

    public static final String ILLEGAL_PARAMETER_FORMAT_RESULT_MESSAGE = "The value of the parameter '%s' does not correspond to the required format: %s";

    private final String parameterName;
    private final String format;

    public IllegalParameterFormatException(String parameterName, String format) {
        super(ILLEGAL_PARAMETER_FORMAT_ERROR_MESSAGE, EX_ILLEGAL_PARAMETER_FORMAT, Collections.singleton(
            new ValidationResult(String.format(ILLEGAL_PARAMETER_FORMAT_RESULT_MESSAGE, parameterName, format), parameterName)));
        this.parameterName = parameterName;
        this.format = format;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getFormat() {
        return format;
    }
}
