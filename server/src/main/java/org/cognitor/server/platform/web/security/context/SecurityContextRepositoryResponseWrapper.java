package org.cognitor.server.platform.web.security.context;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.Assert.notNull;

/**
 * This class is wrappes a <code>HttpServletResponse</code>. By doing this
 * and extending {@link SaveContextOnUpdateOrErrorResponseWrapper} the security
 * context is written on every redirect or error response which is important
 * for the security cookie to work correctly.
 *
 * Nevertheless, it is still important to follow the redirect after submit
 * pattern.
 *
 * @author Patrick Kranz
 */
public class SecurityContextRepositoryResponseWrapper
        extends SaveContextOnUpdateOrErrorResponseWrapper {

    private HttpServletRequest request;
    private SecurityContextRepository repository;

    public SecurityContextRepositoryResponseWrapper(HttpServletRequest request,
        HttpServletResponse response, SecurityContextRepository repository) {
        super(response, false);
        notNull(request);
        notNull(response);
        notNull(repository);
        this.request = request;
        this.repository = repository;
    }

    @Override
    protected void saveContext(SecurityContext context) {
        if (!isCommitted()) {
            repository.saveContext(context, request, (HttpServletResponse) getResponse());
        }
    }
}