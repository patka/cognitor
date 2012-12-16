package org.cognitor.server.platform.user.persistence;

import org.cognitor.server.platform.user.domain.User;

/**
 * @author Patrick Kranz
 */
public interface UserDao {
    void save(User user);

    boolean exists(User user);

    User load(String username);
}
