package org.cognitor.server.platform.user.domain;

/**
 * @author Patrick Kranz
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("User " + username + " already exists.");
    }
}
