package com.huahui.datasphere.mdm.rest.system.exception;

import java.util.Collections;

import com.huahui.datasphere.mdm.system.exception.ExceptionId;
import com.huahui.datasphere.mdm.system.exception.PlatformValidationException;
import com.huahui.datasphere.mdm.system.exception.ValidationResult;

/**
 * An error occurs if a required request parameter is not filled
 *
 * @author theseusyang
 * @since 30.10.2020
 **/
public class MissingParameterException extends PlatformValidationException {
    /**
     * GSVUID.
     */
    private static final long serialVersionUID = -800574927376973004L;

    public static final ExceptionId EX_MISSING_PARAMETER = new ExceptionId("MISSING_PARAMETER", "app.rest.missingParameter");

    public static final String MISSING_PARAMETER_ERROR_MESSAGE = "Required parameter not specified.";
    public static final String MISSING_PARAMETER_RESULT_MESSAGE = "Required parameter not specified: %s.";

    private final String parameterName;

    public MissingParameterException(String parameterName) {
        super(MISSING_PARAMETER_ERROR_MESSAGE, EX_MISSING_PARAMETER,
            Collections.singleton(new ValidationResult(String.format(MISSING_PARAMETER_RESULT_MESSAGE, parameterName), parameterName)));
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
