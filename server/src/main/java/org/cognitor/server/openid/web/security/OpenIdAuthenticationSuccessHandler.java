package org.cognitor.server.openid.web.security;

import org.cognitor.server.openid.web.OpenIdManager;
import org.cognitor.server.platform.security.UniqueKeyUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Patrick Kranz
 */
public class OpenIdAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private OpenIdManager openIdManager;

    public OpenIdAuthenticationSuccessHandler(OpenIdManager manager) {
        this.openIdManager = manager;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        if (isOpenIdAuthentication(request, authentication)) {
            UniqueKeyUserDetails userDetails = (UniqueKeyUserDetails) authentication.getPrincipal();
            String returnUrl = this.openIdManager.getAuthenticationResponseReturnToUrl(request,
                    userDetails.getUniqueKey(),
                    authentication.isAuthenticated());
            response.sendRedirect(returnUrl);
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private boolean isOpenIdAuthentication(HttpServletRequest request,
            Authentication authentication) {
        return this.openIdManager.isOpenIdRequest(request) &&
                UniqueKeyUserDetails.class.isAssignableFrom(
                        authentication.getPrincipal().getClass());
    }
}
