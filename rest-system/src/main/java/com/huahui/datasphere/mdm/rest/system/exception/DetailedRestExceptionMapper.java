package com.huahui.datasphere.mdm.rest.system.exception;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.huahui.datasphere.mdm.system.exception.DomainId;
import com.huahui.datasphere.mdm.system.exception.PlatformBusinessException;
import com.huahui.datasphere.mdm.system.exception.PlatformRuntimeException;
import com.huahui.datasphere.mdm.system.exception.PlatformValidationException;
import com.huahui.datasphere.mdm.system.exception.ValidationResult;
import com.huahui.datasphere.mdm.system.service.TextService;

import com.huahui.datasphere.mdm.rest.system.ro.DetailedErrorResponseRO;
import com.huahui.datasphere.mdm.rest.system.ro.details.ErrorDetailsRO;
import com.huahui.datasphere.mdm.rest.system.ro.details.ResultDetailsRO;

/**
 * Exception mapper.
 * Forms a response body containing the details of the exception
 *
 * @author theseusyang
 * @since 02.11.2020
 **/
public class DetailedRestExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = LoggerFactory.getLogger(DetailedRestExceptionMapper.class);

    private static final int FIRST_LAYER_VALUE = 0;

    private static final int BAD_REQUEST_HTTP_CODE = 400;

    private static final int NOT_ALLOWED_HTTP_CODE = 405;

    private static final int SERVER_ERROR_HTTP_CODE = 500;

    private static final String LAYER_INDENT = "-";

    private static final String ERROR_SEVERITY = "ERROR";

    private static final String CRITICAL_SEVERITY = "CRITICAL";

    private final DomainId defaultDomain;

    @Autowired
    private TextService textService;

    public DetailedRestExceptionMapper(DomainId defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    @Override
    public Response toResponse(Throwable ex) {
        logger.error("Unhandled exception caught: {}", ex.getLocalizedMessage(), ex);
        return Response.status(resolveHttpErrorCode(ex))
            .entity(new DetailedErrorResponseRO(ex.getMessage(), new ResultDetailsRO().withError(createErrorDetails(ex))))
            .type(MediaType.APPLICATION_JSON_TYPE)
            .encoding(StandardCharsets.UTF_8.name())
            .build();
    }

    private Collection<ErrorDetailsRO> createErrorDetails(Throwable ex) {

        List<ErrorDetailsRO> result = new ArrayList<>(4);
        List<Throwable> thl = ExceptionUtils.getThrowableList(ex);
        for (Throwable th : thl) {

            ErrorDetailsRO error;
            if (th instanceof PlatformRuntimeException) {

                error = createPlatformExceptionDetails((PlatformRuntimeException) th);
            } else {

                error = new ErrorDetailsRO();
                error.setInternalMessage(ExceptionUtils.getMessage(th));
                error.setSeverity(CRITICAL_SEVERITY);
                error.setDetails(ExceptionUtils.getStackTrace(th));
            }

            result.add(error);
        }

        return result;
    }

    private ErrorDetailsRO createPlatformExceptionDetails(PlatformRuntimeException ex) {

        ErrorDetailsRO detailsRO = new ErrorDetailsRO();
        DomainId domainId = ObjectUtils.defaultIfNull(ex.getDomain(), defaultDomain);

        detailsRO.setExternalMessage(textService.getText(ex));
        detailsRO.setInternalMessage(ExceptionUtils.getMessage(ex));
        detailsRO.setDomain(domainId.name());
        detailsRO.setSeverity(ERROR_SEVERITY);
        detailsRO.setDetails(ex instanceof PlatformBusinessException ? StringUtils.EMPTY : ExceptionUtils.getStackTrace(ex));

        if (ex instanceof PlatformValidationException) {

            PlatformValidationException pex = (PlatformValidationException) ex;
            Collection<ValidationResult> validations = ObjectUtils.defaultIfNull(pex.getValidationResults(), Collections.emptyList());
            detailsRO.setParams(validations.stream()
                .map(this::formatValidationResultMessage)
                .collect(Collectors.toList()));
        } else {
            detailsRO.setParams(Collections.emptyList());
        }

        return detailsRO;
    }

    private int resolveHttpErrorCode(Throwable ex) {
        int result = SERVER_ERROR_HTTP_CODE;
        if (ex instanceof NotAllowException) {
            result = NOT_ALLOWED_HTTP_CODE;
        } else if (ex instanceof PlatformValidationException) {
            result = BAD_REQUEST_HTTP_CODE;
        }
        return result;
    }

    private String formatValidationResultMessage(ValidationResult res) {
        return formatValidationResultMessage(res, FIRST_LAYER_VALUE);
    }

    private String formatValidationResultMessage(ValidationResult res, int layer) {
        String result = textService.getText(res.getTranslationCode(), res.getArgs());
        Collection<ValidationResult> nested = res.getNestedValidations();
        if (CollectionUtils.isNotEmpty(nested)) {
            String prefix = layer > 0 ? System.lineSeparator() + StringUtils.repeat(LAYER_INDENT, layer) + StringUtils.SPACE : StringUtils.EMPTY;
            String base = result + prefix;
            result = result + prefix + nested.stream()
                .map(nst -> formatValidationResultMessage(nst, layer + 1))
                .collect(Collectors.joining(prefix, base, StringUtils.EMPTY));
        }
        return result;
    }

}
