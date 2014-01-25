package org.cognitor.server.platform.user.service.impl;

import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.persistence.UserDao;
import org.cognitor.server.platform.user.service.UserAlreadyExistsException;
import org.cognitor.server.platform.user.service.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Mock
    private PasswordEncoder passwordEncoderMock;

    private UserServiceImpl service;

    @Before
    public void setUp() {
        service = new UserServiceImpl(userDaoMock, passwordEncoderMock);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowExceptionWhenNoUserForUsernameGiven() {
        when(userDaoMock.load("testUser")).thenReturn(null);

        service.loadUserByUsername("testUser");
    }

    @Test
    public void shouldReturnUserDetailsWhenUserForUsernameGiven() {
        User user = new User("testUser", "password");
        user.setId("1");
        when(userDaoMock.load("testUser")).thenReturn(user);

        UserDetails details = service.loadUserByUsername("testUser");

        assertEquals("testUser", details.getUsername());
        assertEquals("password", details.getPassword());
        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void shouldThrowExceptionWhenAlreadyExistingUserForRegistrationGiven() {
        User testUser = new User("testUser@test.de", "somePass");
        when(userDaoMock.exists(testUser)).thenReturn(true);

        service.registerUser(testUser);
    }

    @Test
    public void shouldCreateNewUserWhenNewUserForRegistrationGiven() {
        User testUser = new User("testUser", "somePass");
        when(passwordEncoderMock.encode("somePass")).thenReturn("encodedPass");

        service.registerUser(testUser);

        assertEquals("encodedPass", testUser.getPassword());
        verify(userDaoMock, times(1)).save(testUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullValueForChangePasswordGiven() {
        service.changePassword(null, "currentPassword");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullValueForCurrentPasswordGiven() {
        service.changePassword(new User("test@test.de", "somePass"), null);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldThrowExceptionWhenUnknownUserForChangePasswordGiven() {
        User testUser = new User("test@test.de", "somePass");
        when(userDaoMock.load("test@test.de")).thenReturn(null);
        service.changePassword(testUser, "somePass");
    }

    @Test(expected = BadCredentialsException.class)
    public void shouldThrowExceptionWhenWrongCurrentPasswordGiven() {
        User testUser = new User("test@test.de", "newPass");
        User persistedUser = new User("test@test.de", "somePass");
        when(userDaoMock.load("test@test.de")).thenReturn(persistedUser);
        when(passwordEncoderMock.encode("newPass")).thenReturn("encodedNewPass");
        when(passwordEncoderMock.matches("somePass", "wrongPassword")).thenReturn(false);

        service.changePassword(testUser, "wrongPassword");
    }

    @Test
    public void shouldReturnUpdatedUserWhenExistingUserForChangePasswordGiven() {
        User testUser = new User("test@test.de", "newPass");
        User persistedUser = new User("test@test.de", "somePass");
        when(userDaoMock.load("test@test.de")).thenReturn(persistedUser);
        when(passwordEncoderMock.encode("newPass")).thenReturn("encodedNewPass");
        when(passwordEncoderMock.matches("somePass", "somePass")).thenReturn(true);

        service.changePassword(testUser, "somePass");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDaoMock, atLeastOnce()).save(userArgumentCaptor.capture());
        assertEquals("encodedNewPass", userArgumentCaptor.getValue().getPassword());
    }
}
