package org.cognitor.server.platform.user.service.impl;

import org.cognitor.server.platform.security.UserDetailsImpl;
import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.persistence.UserDao;
import org.cognitor.server.platform.user.service.UserAlreadyExistsException;
import org.cognitor.server.platform.user.service.UserNotFoundException;
import org.cognitor.server.platform.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import static org.springframework.util.Assert.notNull;

/**
 * @author Patrick Kranz
 */
@Service("UserServiceImpl")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserDao userDao, final PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(@Valid User user) {
        if (userDao.exists(user)) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDao.save(user);
    }

    @Override
    public User changePassword(@Valid User user, String currentPassword) {
        notNull(user);
        notNull(currentPassword);
        User persistentUser = userDao.load(user.getEmail());
        if (persistentUser == null) {
            throw new UserNotFoundException(user.getEmail());
        }
        if (!passwordEncoder.matches(currentPassword, persistentUser.getPassword())) {
            throw new BadCredentialsException("Current password is wrong.");
        }
        persistentUser.setPassword(passwordEncoder.encode(user.getPassword()));
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
