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
     * Saves the given user in the underlying data store.
     *
     * @param user the user that should be saved
     * @return the persisted user entity
     * @throws org.cognitor.server.platform.user.domain.UserAlreadyExistsException
     *  when a new user should be created and the provided email address already
     *  exists
     */
    User save(User user);

    /**
     * Loads the user with the given email address.
     *
     * @param email the email connected with the user
     * @return the persisted {@link User} or null if no user can be found
     */
    User load(String email);
}
