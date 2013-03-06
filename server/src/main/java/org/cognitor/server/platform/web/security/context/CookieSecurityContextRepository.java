package org.cognitor.server.platform.web.security.context;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.Assert.notNull;

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
 * To solve this problem the {@link SecurityContextRepositoryResponseWrapper}
 * which implements {@link SaveContextOnUpdateOrErrorResponseWrapper}
 * is used so that on <code>sendError()</code> and <code>sendRedirect</code>
 * the SecurityContext is persisted to the cookie. Therefore the send redirect
 * after submit pattern should deal with issue.
 *
 * @author Patrick Kranz
 */
public class CookieSecurityContextRepository implements SecurityContextRepository {
    public static final String DEFAULT_COOKIE_NAME = "context";
    private static final Logger LOGGER = LoggerFactory.getLogger(CookieSecurityContextRepository.class);

    private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
    private final SecurityCookieMarshaller securityCookieMarshaller;

    private int sessionDuration = 1800;
    private String cookieName = DEFAULT_COOKIE_NAME;

    public CookieSecurityContextRepository(SecurityCookieMarshaller marshaller) {
        notNull(marshaller);
        this.securityCookieMarshaller = marshaller;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        wrapResponse(requestResponseHolder);
        LOGGER.debug("Trying to load security context from request.");

        Cookie securityCookie = getCookieForName(requestResponseHolder.getRequest().getCookies(),
                cookieName);

        if (securityCookie == null) {
            LOGGER.debug("No security cookie found in request. Returning empty context.");
            return createNewContext();
        }

        LOGGER.debug("Security cookie found, trying to deserialize");
        SecurityCookie cookie = securityCookieMarshaller.getSecurityCookie(securityCookie.getValue());
        if (cookie == null || !cookie.isValid()) {
            LOGGER.debug("Security cookie was not valid. Returning empty context.");
            return createNewContext();
        }

        LOGGER.debug("Returning context from cookie.");
        SecurityContext context = cookie.getSecurityContext();
        renewContext(context, requestResponseHolder);
        return context;
    }

    /**
     * This method is required, if the user is just browsing the site, we need to make
     * sure, the session duration in the cookie is refreshed and since a cookie can only
     * be written as long as no content was sent to the user, it needs to happen here.
     */
    private void renewContext(SecurityContext context, HttpRequestResponseHolder holder) {
        saveContext(context, holder.getRequest(), holder.getResponse());
    }

    private void wrapResponse(HttpRequestResponseHolder requestResponseHolder) {
        requestResponseHolder.setResponse(new SecurityContextRepositoryResponseWrapper(
                requestResponseHolder.getRequest(), requestResponseHolder.getResponse(), this));
    }

    private static SecurityContext createNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        if (response.isCommitted()) {
            return;
        }

        if (isAnonymous(context.getAuthentication())) {
            LOGGER.debug("Not persisting anonymous authentication to cookie.");
            return;
        }

        SecurityCookie cookie = new SecurityCookie(context, calculateValidUntil());
        String value = securityCookieMarshaller.getBase64EncodedValue(cookie);
        if (value != null && !value.isEmpty()) {
            addCookieToResponse(response, value);
        }
    }

    private boolean isAnonymous(Authentication authentication) {
        return authentication == null || authenticationTrustResolver.isAnonymous(authentication);
    }

    private DateTime calculateValidUntil() {
        return DateTime.now().plusSeconds(sessionDuration);
    }

    private void addCookieToResponse(HttpServletResponse response, String encodedData) {
        Cookie securityCookie = new Cookie(cookieName, encodedData);
        securityCookie.setMaxAge(sessionDuration);
        securityCookie.setPath("/");
        response.addCookie(securityCookie);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return getCookieForName(request.getCookies(), cookieName) != null;
    }

    private static Cookie getCookieForName(Cookie[] cookies, String cookieName) {
        if (cookies == null) {
            return null;
        }
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

    public void setSessionDuration(int sessionDurationInSeconds) {
        this.sessionDuration = sessionDurationInSeconds;
    }
}