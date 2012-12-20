package org.cognitor.server.openid.web.security;

import org.cognitor.server.openid.web.OpenIdManager;
import org.cognitor.server.platform.security.UniqueKeyUserDetails;
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

        if (isOpenIdAuthentication(request, authentication)) {
            UniqueKeyUserDetails userDetails = (UniqueKeyUserDetails) authentication.getDetails();
            String returnUrl = this.openIdManager.getAuthenticationResponseReturnToUrl(request,
                    userDetails.getUniqueKey(),
                    authentication.isAuthenticated());
            response.sendRedirect(returnUrl);
        }
    }

    private boolean isOpenIdAuthentication(HttpServletRequest request,
            Authentication authentication) {
        return this.openIdManager.isOpenIdRequest(request) ||
                UniqueKeyUserDetails.class.isAssignableFrom(
                        authentication.getDetails().getClass());
    }
}
