/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cognitor.server.platform.user.service;

import org.cognitor.server.platform.user.domain.User;

/**
 * @author patrick
 */
public interface UserService {

    void registerUser(User user);
}
