package org.cognitor.server.platform.user.service;

/**
 * This exception is thrown in case somebody tries to create a user
 * that is already present in the data store.
 *
 * @author Patrick Kranz
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("User " + username + " already exists.");
    }
}
