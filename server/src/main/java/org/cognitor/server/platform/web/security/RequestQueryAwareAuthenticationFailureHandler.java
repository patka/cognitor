package org.cognitor.server.platform.web.security;

import org.cognitor.server.platform.web.util.UrlUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirects the user to the default failure url (currently "/")
 * and keeps all request parameters.
 *
 * @author Patrick Kranz
 */
public class RequestQueryAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final String DEFAULT_FAILURE_URL = "/";
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        StringBuilder redirectUrlBuilder = new StringBuilder(DEFAULT_FAILURE_URL);
        redirectUrlBuilder.append(UrlUtil.createQueryString(request.getQueryString()));

        redirectStrategy.sendRedirect(request, response, redirectUrlBuilder.toString());
    }

    public void setRedirectStrategy(RedirectStrategy strategy) {
        this.redirectStrategy = strategy;
    }
}
