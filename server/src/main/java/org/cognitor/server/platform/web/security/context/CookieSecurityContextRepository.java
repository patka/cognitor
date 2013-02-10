package org.cognitor.server.platform.web.security.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;

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
    private static final String COOKIE_DATA_ENCODING = "UTF-8";

    private final SecurityContextSerializer cookieSerializer;

    private String cookieName = DEFAULT_COOKIE_NAME;

    public CookieSecurityContextRepository(SecurityContextSerializer serializer) {
        notNull(serializer);
        this.cookieSerializer = serializer;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        wrapResponse(requestResponseHolder);
        LOGGER.debug("Trying to load security context from request.");

        Cookie securityCookie = getCookieForName(requestResponseHolder.getRequest().getCookies(),
                cookieName);

        if (noSecurityCookieFound(securityCookie)) {
            LOGGER.debug("No security cookie found. Returning new context.");
            return createNewContext();
        }

        return getSecurityContext(securityCookie);
    }

    private void wrapResponse(HttpRequestResponseHolder requestResponseHolder) {
        requestResponseHolder.setResponse(new SecurityContextRepositoryResponseWrapper(
                requestResponseHolder.getRequest(), requestResponseHolder.getResponse(), this));
    }

    private boolean noSecurityCookieFound(Cookie securityCookie) {
        return securityCookie == null;
    }

    private SecurityContext getSecurityContext(Cookie securityCookie) {
        try {
            SecurityContext context =
                    cookieSerializer.deserialize(decodeBase64(securityCookie.getValue()));
            LOGGER.debug("Successfully loaded security context from cookie");
            return context;
        } catch (SerializeException exception) {
            return createNewContext();
        }
    }


    private static byte[] decodeBase64(String base64encodedData) {
        try {
            return Base64.decode(base64encodedData.getBytes(COOKIE_DATA_ENCODING));
        } catch (UnsupportedEncodingException exception) {
            LOGGER.error("No " + COOKIE_DATA_ENCODING + " support found on this system! Login will not work.", exception);
            throw new SerializeException("Cookie data cannot be decoded", exception);
        }
    }

    private static SecurityContext createNewContext() {
        return SecurityContextHolder.createEmptyContext();
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        byte[] serializedData = null;
        try {
            serializedData = cookieSerializer.serialize(context);
            String encodedData = encodeBase64(serializedData);
            addCookieToResponse(response, encodedData);
        } catch (SerializeException exception) {
            LOGGER.error("Serialization of security context failed. No cookie set.", exception);
        }
    }

    private void addCookieToResponse(HttpServletResponse response, String encodedData) {
        Cookie securityCookie = new Cookie(cookieName, encodedData);
        securityCookie.setPath("/");
        response.addCookie(securityCookie);
    }

    private String encodeBase64(byte[] serializedData) {
        try {
            byte[] base64encodedData = Base64.encode(serializedData);
            return new String(base64encodedData, COOKIE_DATA_ENCODING);
        } catch (UnsupportedEncodingException exception) {
            LOGGER.error("No " + COOKIE_DATA_ENCODING + " support found on this system! Login will not work.", exception);
            return "";
        }
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
}