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
     * is capable of handling new entities as well as updating existing ones.
     *
     * If a new entity is saved the returned entity will have the <code>Id</code>
     * property set.
     *
     * @param user the user that should be saved. A user with an empty <code>Id</code>
     *             property is considered to be new. Passing in a user with
     *             a value in the <code>Id</code> property is updated.
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
