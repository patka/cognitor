package org.cognitor.server.openid.web.controller;

import org.cognitor.server.openid.web.XrdsDocumentBuilder;
import org.cognitor.server.openid.web.OpenIdManager;
import org.cognitor.server.openid.web.OpenIdMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

import static org.cognitor.server.openid.web.OpenIdManager.XRDS_HEADER;
import static org.cognitor.server.platform.web.util.UrlUtil.createQueryString;

/**
 * User: patrick
 * Date: 13.11.12
 */
@Controller
public class OpenIdController {

    private OpenIdManager openIdManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenIdController.class);
    private static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
    private static final String MIME_TYPE_XRDS_XML = "application/xrds+xml";

    @Autowired
    public OpenIdController(OpenIdManager manager) {
        this.openIdManager = manager;
    }

    @RequestMapping(value="/sso/authenticate", method={ RequestMethod.GET, RequestMethod.POST })
    public void dispatchOpenIdRequest(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    Authentication authentication) throws IOException {

        OpenIdMode requestMode = openIdManager.getMode(request);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Received request with openid mode " + requestMode);
        }

        switch (requestMode) {
            case ASSOCIATE:
                handleAssociation(request, response);
                break;
            case CHECKID_SETUP:
            case CHECKID_IMMEDIATE:
                handleCheckId(request, response, authentication, requestMode);
                break;
            case CHECK_AUTHENTICATION:
                handleCheckAuthentication(request, response);
                break;
            case DISCOVERY:
                sendDiscoveryXml(request, response);
                break;
        }
    }

    @RequestMapping(value="/sso/authenticate", method=RequestMethod.HEAD)
    public void sendXrdsLocation(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder headerInformation = new StringBuilder(request.getRequestURL().toString());

        headerInformation.append(createQueryString(request.getQueryString()));

        response.addHeader(XRDS_HEADER, headerInformation.toString());
    }

    private void handleCheckId(HttpServletRequest request, HttpServletResponse response,
                               Authentication authentication, OpenIdMode requestMode)
            throws IOException {
        if (isUserAuthenticated(authentication)) {
            response.sendRedirect(
                    openIdManager.getAuthenticationResponseReturnToUrl(request, authentication.getName(), true));
        } else {
            if (OpenIdMode.CHECKID_SETUP.equals(requestMode)) {
                response.sendRedirect(openIdManager.getLoginUrl(request));
            } else {
                response.sendRedirect(openIdManager.getAuthenticationResponseReturnToUrl(request,
                                    request.getRequestURL().toString(), false));
            }
        }
    }

    private void sendDiscoveryXml(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        XrdsDocumentBuilder documentBuilder = new XrdsDocumentBuilder();

        if (request.getParameterMap().containsKey("id")) {
            documentBuilder.addSignOnSerice(request.getRequestURL().toString());
        } else {
            documentBuilder.addServerService(request.getRequestURL().toString());
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending xrds document:\n" + documentBuilder.getDocumentAsString());
        }
        writeToResponse(response, documentBuilder.getDocumentAsString(), MIME_TYPE_XRDS_XML);
    }

    private void handleCheckAuthentication(final HttpServletRequest request,
                                           final HttpServletResponse response)
        throws IOException {
        String responseValue = openIdManager.verify(request);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending verification response: " + responseValue);
        }
        writeToResponse(response, responseValue, MIME_TYPE_TEXT_PLAIN);
    }

    private void handleAssociation(final HttpServletRequest request,
                                   final HttpServletResponse response)
        throws IOException {
        String responseValue = openIdManager.associate(request);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Sending association response: " + responseValue);
        }
        writeToResponse(response, responseValue, MIME_TYPE_TEXT_PLAIN);
    }

    private static boolean isUserAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }

    private static void writeToResponse(HttpServletResponse response, String responseValue,
                                               String contentType)
        throws IOException {
        response.setContentType(contentType);
        Writer writer = response.getWriter();
        writer.write(responseValue);
        writer.flush();
    }
}
