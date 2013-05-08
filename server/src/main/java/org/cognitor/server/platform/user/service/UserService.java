/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cognitor.server.platform.user.service;

import org.cognitor.server.platform.user.domain.User;

/**
 * @author Patrick Kranz
 */
public interface UserService {

    /**
    * Registers a new user in the system.
    *
    * @throws {@link UserAlreadyExistsException}
    *  when a new user should be created and the provided email address already
    *  exists
    */
    User registerUser(User user);

     /**
     * Changes the password for the given user with the password provided
     * in the given {@link User} object.
     */
    User changePassword(User user);
}
