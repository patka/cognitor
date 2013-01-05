package org.cognitor.server.platform.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Patrick Kranz
 */
public interface UniqueKeyUserDetails extends UserDetails {
    /**
     * Represents a unique key that, unlike the username,
     * should never change as long as the user exists in the
     * system.
     *
     * @return a system wide immutable unique key
     */
    String getUniqueKey();
}
