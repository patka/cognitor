package org.cognitor.server.platform.user.service.impl;

import org.cognitor.server.platform.security.UserDetailsImpl;
import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.persistence.UserDao;
import org.cognitor.server.platform.user.service.UserAlreadyExistsException;
import org.cognitor.server.platform.user.service.UserNotFoundException;
import org.cognitor.server.platform.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.util.Assert.notNull;

/**
 * @author Patrick Kranz
 */
@Service("UserServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User registerUser(User user) {
        if (userDao.exists(user)) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        return userDao.save(user);
    }

    @Override
    public User changePassword(User user) {
        notNull(user);
        User persistentUser = userDao.load(user.getEmail());
        if (persistentUser == null) {
            throw new UserNotFoundException(user.getEmail());
        }
        persistentUser.setPassword(user.getPassword());
        return userDao.save(persistentUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        final User user = userDao.load(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
        return new UserDetailsImpl(user.getEmail(), user.getPassword(), user.getId());
    }
}
