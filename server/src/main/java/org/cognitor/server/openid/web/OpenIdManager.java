package org.cognitor.server.openid.web;

import org.openid4java.message.ParameterList;
import org.openid4java.server.ServerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Patrick Kranz
 */
@Component
public class OpenIdManager {
    public static final String XRDS_HEADER = "X-XRDS-Location";
    public static final String MODE = "openid.mode";

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenIdManager.class);

    private final ServerManager serverManager;

    @Autowired
    public OpenIdManager(ServerManager manager) {
        this.serverManager = manager;
    }

    public boolean isOpenIdRequest(HttpServletRequest request) {
        return request.getParameter(MODE) != null;
    }

    public OpenIdMode getMode(HttpServletRequest request) {
        if (!isOpenIdRequest(request)) {
            return OpenIdMode.DISCOVERY;
        }

        String modeString = request.getParameter(MODE);
        try {
            return OpenIdMode.valueOf(modeString.toUpperCase());
        } catch (IllegalArgumentException exception) {
            LOGGER.warn("Unknown mode " + modeString + " given in request.");
            return OpenIdMode.DISCOVERY;
        }
    }

    public String getAuthenticationResponseReturnToUrl(HttpServletRequest request, String localId, boolean isAuthenticated) {
        return serverManager.authResponse(getParameterList(request), getSsoIdentity(localId),
                getSsoIdentity(localId), isAuthenticated, true).getDestinationUrl(true);
    }

    public String verify(HttpServletRequest request) {
        return serverManager.verify(getParameterList(request)).keyValueFormEncoding();
    }

    public String associate(HttpServletRequest request) {
        return serverManager.associationResponse(getParameterList(request)).keyValueFormEncoding();
    }

    public String getLoginUrl(HttpServletRequest request) {
        String requestQueryString = request.getQueryString();
        StringBuilder redirectUrl = new StringBuilder(serverManager.getUserSetupUrl());
        if (StringUtils.hasText(request.getQueryString())) {
            redirectUrl.append("?").append(requestQueryString);
        }
        return redirectUrl.toString();
    }

    private static ParameterList getParameterList(HttpServletRequest request) {
        return new ParameterList(request.getParameterMap());
    }

    private String getSsoIdentity(String localId) {
        StringBuilder ssoId = new StringBuilder(serverManager.getOPEndpointUrl());
        return ssoId.append("?id=").append(localId).toString();
    }
}
