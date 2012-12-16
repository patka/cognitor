package org.cognitor.server.openid.web.security;

import org.cognitor.server.openid.web.OpenIdManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Patrick Kranz
 */
public class OpenIdAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private OpenIdManager openIdManager;

    public OpenIdAuthenticationSuccessHandler(OpenIdManager manager) {
        this.openIdManager = manager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        if (!this.openIdManager.isOpenIdRequest(request)) {
            return;
        }

        String returnUrl = this.openIdManager.getAuthenticationResponseReturnToUrl(request,
                authentication.getName(),
                authentication.isAuthenticated());
        response.sendRedirect(returnUrl);
    }
}
