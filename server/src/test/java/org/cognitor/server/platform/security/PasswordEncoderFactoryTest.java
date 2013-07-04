package org.cognitor.server.platform.security;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordEncoderFactoryTest {
    @Mock
    private Appender appenderMock;

    @Before
    public void setup() {
        Logger.getRootLogger().addAppender(appenderMock);
    }

    @After
    public void tearDown() {
        Logger.getRootLogger().removeAppender(appenderMock);
    }

    @Test
    public void shouldReturnBcryptPasswordEncoderWhenBcryptGiven() {
        PasswordEncoder encoder = PasswordEncoderFactory.getPasswordEncoder("bcrypt");
        assertTrue(encoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void shouldReturnNoOpEncoderWhenNoHashGiven() {
        PasswordEncoder encoder = PasswordEncoderFactory.getPasswordEncoder("no_hash");
        assertTrue(encoder instanceof NoOpPasswordEncoder);
    }

    @Test
    public void shouldReturnStandardPasswordEncoderWhenSha256Given() {
        PasswordEncoder encoder = PasswordEncoderFactory.getPasswordEncoder("sha_256");
        assertTrue(encoder instanceof StandardPasswordEncoder);
    }

    @Test
    public void shouldReturnNoOpEncoderWhenUnknownParameterGiven() {
        PasswordEncoder encoder = PasswordEncoderFactory.getPasswordEncoder("typo");
        assertTrue(encoder instanceof NoOpPasswordEncoder);
    }

    @Test
    public void shouldLogErrorWhenUnknownParameterGiven() {
        PasswordEncoderFactory.getPasswordEncoder("nonSense");
        verifyErrorWasLogged();
    }

    private void verifyErrorWasLogged() {
        ArgumentCaptor<LoggingEvent> eventCaptor = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(eventCaptor.capture());

        LoggingEvent event = eventCaptor.getValue();
        assertEquals(Level.ERROR, event.getLevel());
        assertTrue(event.getRenderedMessage().startsWith("No password encoder"));
    }

    @Test
    public void shouldReturnNoOpEncoderWhenNullGiven() {
        PasswordEncoder encoder = PasswordEncoderFactory.getPasswordEncoder(null);

        verifyErrorWasLogged();
        assertTrue(encoder instanceof NoOpPasswordEncoder);
    }
}
