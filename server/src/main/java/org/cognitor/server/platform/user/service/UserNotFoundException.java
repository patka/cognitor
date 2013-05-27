package org.cognitor.server.platform.user.service;

/**
 * @author Patrick Kranz
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("The user " + username + " was not found.");
    }
}
