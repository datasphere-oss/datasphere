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
package com.huahui.datasphere.platform.rest.core.interceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.huahui.datasphere.portal.service.SecurityService;
import com.huahui.datasphere.portal.type.security.SecurityDataSource;

/**
 * The Class TokenInterceptor.
 */
public class BearerTokenCxfInterceptor extends AbstractPhaseInterceptor<Message> {

    /** Login URI. */
    private static final String URI_LOGIN = "/login";
    /** Logout URI. */
    private static final String URI_LOGOUT = "/logout";
    /** Forgot password URI. */
    private static final String URI_FORGOT_PASSWORD = "/forgot-password";
    /** ACTIVATE password URI. */
    private static final String URI_ACTIVATE_PASSWORD = "/activate-password";
    /** API-Docs API. */
    private static final String URI_HEALTH_CHECK = "/healthcheck";
    /** API-Docs API. */
    private static final String URI_API_DOCS = "/api-docs";
    /** API-Docs API. */
    private static final String URI_API_SCRIPTS = "/swagger-";
    /** API-Docs endpoint description. */
    private static final String URI_API_DOCS_ENDPOINT = "openapi.json";
    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BearerTokenCxfInterceptor.class);
    /**
     * The security service.
     */
    private final SecurityService securityService;
    /**
     * Instantiates a new token interceptor.
     */
    public BearerTokenCxfInterceptor(final SecurityService securityService) {
        super(Phase.RECEIVE);
        LOGGER.info("Register security interceptor {}", BearerTokenCxfInterceptor.class.getCanonicalName());
        this.securityService = securityService;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message
     * .Message)
     */
    @Override
    public void handleMessage(Message inMessage) {

        String requestURI = (String) inMessage.get(Message.REQUEST_URI);
        String queryString = (String) inMessage.get(Message.QUERY_STRING);
        String type = (String) inMessage.get(Message.CONTENT_TYPE);

        // don't check token if
        if (passThrough(type, requestURI, queryString)) {
            return;
        }

        final HttpServletRequest request = (HttpServletRequest) inMessage.get("HTTP.REQUEST");
        final RequestHandleResult result = handleInternal(request);

        if (result instanceof AuthRequestHandleResult) {
            inMessage.getExchange().put(Authentication.class, SecurityContextHolder.getContext().getAuthentication());

            if (result instanceof AuthAndRedirectRequestHandleResult) {
                handleRedirectResult(
                        inMessage,
                        ((AuthAndRedirectRequestHandleResult) result).getRedirectTokenCheckResult()
                );
            }
            return;
        }

        if (result instanceof NotAuthRequestHandleResult) {
        	Fault fault = new Fault("Token not valid", java.util.logging.Logger.getGlobal());
        	fault.setStatusCode(HttpURLConnection.HTTP_UNAUTHORIZED);
        	throw fault;
        }

        if (result instanceof RedirectRequestHandleResult) {
            RedirectRequestHandleResult redirectTokenCheckResult = (RedirectRequestHandleResult) result;

            handleRedirectResult(inMessage, redirectTokenCheckResult);
        }
    }

    private void handleRedirectResult(Message inMessage, RedirectRequestHandleResult redirectTokenCheckResult) {
        Message outMessage = getOutMessage(inMessage);
        outMessage.put(AbstractHTTPDestination.REQUEST_REDIRECTED, true);
        outMessage.put(Message.RESPONSE_CODE, HttpURLConnection.HTTP_MOVED_PERM);
        Map<String, List<String>> headers = CastUtils.cast((Map<?, ?>) outMessage.get(Message.PROTOCOL_HEADERS));

        headers.put("Location", Collections.singletonList(redirectTokenCheckResult.getLocation()));
        headers.putAll(redirectTokenCheckResult.getHeaders());

        stopChain(inMessage, outMessage);
    }

    private void stopChain(Message inMessage, Message outMessage) {
        inMessage.getInterceptorChain().abort();
        try {
            getConduit(inMessage).prepare(outMessage);
            close(outMessage);
        } catch (IOException ioe) {
            throw new Fault(ioe);
        }
    }

    private RequestHandleResult handleInternal(final HttpServletRequest request) {

        for (SecurityDataSource sds : securityService.getSecurityDataSources()) {

            if (Objects.isNull(sds.getInterceptionProvider())) {
                continue;
            }

            RequestHandleResult checkResult = sds.getInterceptionProvider().handleRequest(request);
            if (!(checkResult instanceof NotAuthRequestHandleResult)) {
                return checkResult;
            }
        }

        return NotAuthRequestHandleResult.get();
    }

    /**
     * Paa-through without token check.
     * @param type request type
     * @param uri request URI
     * @param queryString the query string
     * @return true for skip, flase otherwise
     */
    private boolean passThrough(String type, String uri, String queryString) {

        if (StringUtils.contains(type, MediaType.TEXT_XML)) {
            return true;
        }

        if (uri.contains(URI_LOGIN) || uri.contains(URI_LOGOUT) || uri.contains(URI_HEALTH_CHECK)
         || uri.contains(URI_FORGOT_PASSWORD) || uri.contains(URI_ACTIVATE_PASSWORD)
         || uri.contains(URI_API_DOCS) || uri.endsWith(URI_API_DOCS_ENDPOINT) || uri.contains(URI_API_SCRIPTS)) {
            return true;
        }

        return "wsdl".equals(queryString) || StringUtils.startsWith(queryString, "xsd=");
    }
    /**
     * Gets the out message.
     *
     * @param inMessage
     *            the in message
     * @return the out message
     */
    private Message getOutMessage(Message inMessage) {
        Exchange exchange = inMessage.getExchange();
        Message outMessage = exchange.getOutMessage();
        if (outMessage == null) {
            Endpoint endpoint = exchange.get(Endpoint.class);
            outMessage = endpoint.getBinding().createMessage();
            exchange.setOutMessage(outMessage);
        }
        outMessage.putAll(inMessage);
        return outMessage;
    }

    /**
     * Gets the conduit.
     *
     * @param inMessage
     *            the in message
     * @return the conduit
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private Conduit getConduit(Message inMessage) throws IOException {
        Exchange exchange = inMessage.getExchange();
        Conduit conduit = exchange.getDestination().getBackChannel(inMessage);
        exchange.setConduit(conduit);
        return conduit;
    }

    /**
     * Close.
     *
     * @param outMessage
     *            the out message
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void close(Message outMessage) throws IOException {
        OutputStream os = outMessage.getContent(OutputStream.class);
        os.flush();
        os.close();
    }
}
