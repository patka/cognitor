package org.cognitor.server.platform.web.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.cognitor.server.platform.web.util.UrlUtil.createQueryString;

/**
 * Redirects the user to the default failure url (currently "/")
 * and keeps all request parameters.
 *
 * @author Patrick Kranz
 */
public class RequestQueryAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private String defaultFailureUrl = "/loginFailed.html";

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String redirectUrl = defaultFailureUrl + createQueryString(request.getQueryString());
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

    public void setRedirectStrategy(RedirectStrategy strategy) {
        this.redirectStrategy = strategy;
    }

    public void setDefaultFailureUrl(String url) {
        if (StringUtils.hasText(url)) {
            this.defaultFailureUrl = url;
        }
    }
}
