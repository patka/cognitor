package org.cognitor.server.platform.user.service.impl;

import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.persistence.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserDao userDaoMock;

    private UserServiceImpl service;

    @Before
    public void setUp() {
        service = new UserServiceImpl(userDaoMock);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowExceptionWhenNoUserForUsernameGiven() {
        when(userDaoMock.load("testUser")).thenReturn(null);

        service.loadUserByUsername("testUser");
    }

    @Test
    public void shouldReturnUserDetailsWhenUserForUsernameGiven() {
        when(userDaoMock.load("testUser")).thenReturn(new User("testUser", "password"));

        UserDetails details = service.loadUserByUsername("testUser");

        assertEquals("testUser", details.getUsername());
        assertEquals("password", details.getPassword());
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenAlreadyExistingUserForRegistrationGiven() {
        User testUser = new User("testUser", "somePass");
        when(userDaoMock.exists(testUser)).thenReturn(true);

        service.registerUser(testUser);
    }

    @Test
    public void shouldCreateNewUserWhenNewUserForRegistrationGiven() {
        User testUser = new User("testUser", "somePass");
        when(userDaoMock.exists(testUser)).thenReturn(false);

        service.registerUser(testUser);

        verify(userDaoMock, times(1)).save(testUser);
    }
}
