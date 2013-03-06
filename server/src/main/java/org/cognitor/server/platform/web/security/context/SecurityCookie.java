package org.cognitor.server.platform.web.security.context;

import org.joda.time.DateTime;
import org.springframework.security.core.context.SecurityContext;

import static org.springframework.util.Assert.notNull;

/**
 * A container for the data that will be persisted in the
 * security cookie.
 *
 * @author Patrick Kranz
 */
public class SecurityCookie {
    private final SecurityContext securityContext;
    private final DateTime validUntil;

    public SecurityCookie(SecurityContext context, DateTime validUntil) {
        notNull(context);
        notNull(validUntil);
        this.securityContext = context;
        this.validUntil = validUntil;
    }

    public DateTime getValidUntil() {
        return validUntil;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public boolean isValid() {
        return DateTime.now().isBefore(this.validUntil);
    }
}
