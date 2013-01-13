package org.cognitor.server.platform.web.security.context;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This implementation of Springs <code>SecurityContextRepository</code> stores
 * the security context into a cookie and therefore makes it possible to abstain
 * from using a Http Session and therefore allows the application to be stateless
 * from a security point of view with all the benefits this brings.
 *
 * There is, however, one limitation:
 * Since a cookie is used to persist the security context, all changes to the
 * security context need to be done <em>before</em> any content is written to
 * the http response since the cookie is part of the response header.
 *
 * This means, that normally, after changes to the security context you will
 * need to call saveContext again since the
 * {@link org.springframework.security.web.context.SecurityContextPersistenceFilter}
 * saves the security context <em>after</em> the request has been served.
 *
 * The redirect after submit pattern is recommended here to ease this issue.
 *
 * @author Patrick Kranz
 */
public class CookieSecurityContextRepository implements SecurityContextRepository {
    public static final String DEFAULT_COOKIE_NAME = "context";

    private String cookieName = DEFAULT_COOKIE_NAME;

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        return cookies != null && getCookieForName(cookies, cookieName) != null;
    }

    private static Cookie getCookieForName(Cookie[] cookies, String cookieName) {
        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    public void setCookieName(String cookieName) {
        if (cookieName == null || cookieName.isEmpty()) {
            throw new IllegalArgumentException("Cookie name must not be empty.");
        }
        this.cookieName = cookieName;
    }
}
