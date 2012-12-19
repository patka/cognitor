package org.cognitor.server.platform.user.service.impl;

import org.cognitor.server.platform.security.UserDetailsImpl;
import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.persistence.UserDao;
import org.cognitor.server.platform.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author patrick
 */
@Service(value = "UserServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public void registerUser(User user) {
        if (userDao.exists(user)) {
            throw new IllegalStateException("User already existing");
        }

        userDao.save(user);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        final User user = userDao.load(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found.");
        }
        return new UserDetailsImpl(user);
    }
}
