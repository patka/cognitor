package org.cognitor.server.platform.user.persistence;

import org.cognitor.server.platform.user.domain.User;

/**
 * Provides the required data access operations for the
 * {@link User} domain object.
 *
 * @author Patrick Kranz
 */
public interface UserDao {
    /**
     * Saves the given user in the underlying data store. This method
     * is capable of handling new entities as well as saving new ones.
     *
     * @param user the user that should be saved
     * @return the persisted user entity
     */
    User save(User user);

    /**
     * Checks if the given user is already present in the underlying
     * data store.
     *
     * @param user the user to be checked
     * @return true if the user is known to the system, false otherwise
     */
    boolean exists(User user);

    /**
     * Loads the user with the given email address.
     *
     * @param email the email connected with the user
     * @return the persisted {@link User} or null if no user can be found
     */
    User load(String email);
}
