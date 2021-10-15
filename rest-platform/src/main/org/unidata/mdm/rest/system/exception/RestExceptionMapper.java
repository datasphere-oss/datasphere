/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package org.unidata.mdm.rest.system.exception;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidata.mdm.system.exception.PlatformBusinessException;
import org.unidata.mdm.system.exception.PlatformRuntimeException;
import org.unidata.mdm.system.exception.PlatformValidationException;
import org.unidata.mdm.system.exception.ValidationResult;
import org.unidata.mdm.system.util.TextUtils;

import com.huahui.datasphere.rest.system.ro.ErrorInfo;
import com.huahui.datasphere.rest.system.ro.ErrorResponse;

/**
 * @author Alexander Malyshev
 */
public class RestExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionMapper.class);

    private static final String LAYER_INDENT = "-";

    @Override
    public Response toResponse(Throwable exception) {
        logger.error("Error while executing request", exception);

        ErrorResponse response = buildErrorResponse(exception);

        return Response
                .status(Response.Status.OK)
                .entity(response)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .encoding(StandardCharsets.UTF_8.name())
                .build();
    }

    private ErrorResponse buildErrorResponse(Throwable exception) {
        // TODO: 10.06.2020 Comrades! if you see more than 3 "if instanceof", then refactor the code as a factory
        if (exception instanceof PlatformValidationException) {
            return convertValidationExceptionResponse((PlatformValidationException) exception);
        } else if (exception instanceof PlatformBusinessException) {
            return convertRuntimeExceptionResponse((PlatformRuntimeException) exception);
        } else {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.setInternalMessage(exception.getMessage());
            errorInfo.setErrorCode(null);
            errorInfo.setUserMessage(exception.getMessage());

            errorInfo.setSeverity(ErrorInfo.Severity.CRITICAL);

            ErrorResponse response = new ErrorResponse();
            response.setErrors(Collections.singletonList(errorInfo));


            return response;
        }
    }

    private ErrorResponse convertRuntimeExceptionResponse(final PlatformRuntimeException exception) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setInternalMessage(exception.getMessage());
        errorInfo.setUserMessage(TextUtils.getText(exception.getId().message(), exception.getArgs()));
        errorInfo.setSeverity(ErrorInfo.Severity.NORMAL);
        errorInfo.setErrorCode(exception.getId().code());
        ErrorResponse response = new ErrorResponse();
        response.setErrors(Collections.singletonList(errorInfo));
        return response;
    }

    private ErrorResponse convertValidationExceptionResponse(final PlatformValidationException exception) {
        ErrorInfo errorInfo;
        errorInfo = new ErrorInfo(ErrorInfo.Type.VALIDATION_ERROR);

        final String message = TextUtils.getText(exception.getMessage());
        errorInfo.setUserMessage(message);

        if (CollectionUtils.isNotEmpty(exception.getValidationResults())) {
            List<String> validationMessages = exception
                    .getValidationResults()
                    .stream()
                    .map(res -> getValidationResultMessage(res, 1))
                    .collect(Collectors.toList());

            if (validationMessages.size() == 1) {
                errorInfo.setUserMessage(
                        message + System.lineSeparator() + LAYER_INDENT + " " + validationMessages.get(0)
                );
            } else {
                String overAllMessage = validationMessages
                        .stream()
                        .collect(
                                Collectors.joining(
                                        System.lineSeparator() + LAYER_INDENT + " ",
                                        System.lineSeparator() + LAYER_INDENT + " ",
                                        ""
                                )
                        );
                errorInfo.setUserMessageDetails(message + overAllMessage);
            }
        }

        errorInfo.setSeverity(ErrorInfo.Severity.NORMAL);
        errorInfo.setErrorCode(exception.getId().code());

        errorInfo.setInternalMessage(exception.getMessage());
        errorInfo.setSeverity(ErrorInfo.Severity.CRITICAL);

        ErrorResponse response = new ErrorResponse(exception);

        response.setErrors(Collections.singletonList(errorInfo));

        return response;
    }

    private String getValidationResultMessage(ValidationResult res, int layer) {
        String mainMessage = TextUtils.getText(res.getTranslationCode(), res.getArgs());
        if (CollectionUtils.isNotEmpty(res.getNestedValidations())) {
            String prefix = System.lineSeparator();
            prefix += StringUtils.repeat(LAYER_INDENT, layer);
            prefix += " ";

            mainMessage = mainMessage + prefix;
            return res.getNestedValidations()
                    .stream()
                    .map(res1 -> getValidationResultMessage(res1, layer + 1))
                    .collect(Collectors.joining(prefix, mainMessage, ""));
        } else {
            return mainMessage;
        }
    }
}
