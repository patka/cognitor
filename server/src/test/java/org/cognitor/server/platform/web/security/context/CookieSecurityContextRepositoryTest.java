package org.cognitor.server.platform.web.security.context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.cognitor.server.platform.web.security.context.CookieSecurityContextRepository.DEFAULT_COOKIE_NAME;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class CookieSecurityContextRepositoryTest {
    @Mock
    HttpServletRequest requestMock;

    private CookieSecurityContextRepository repository;

    @Before
    public void setUp() {
        this.repository = new CookieSecurityContextRepository();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullValueForCookieNameGiven() {
        repository.setCookieName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenEmptyStringValueForCookieNameGiven() {
        repository.setCookieName("");
    }

    @Test
    public void shouldReturnFalseWhenRequestWithNoCookiesGiven() {
        when(requestMock.getCookies()).thenReturn(null);
        assertFalse(repository.containsContext(requestMock));
    }

    @Test
    public void shouldReturnFalseWhenRequestWithNoSecurityCookieGiven() {
        when(requestMock.getCookies()).thenReturn(new Cookie[]{ new Cookie("name", "value")});
        assertFalse(repository.containsContext(requestMock));
    }

    @Test
    public void shouldReturnTrueWhenRequestWithSecurityCookieGiven() {
        when(requestMock.getCookies()).thenReturn(new Cookie[]{ new Cookie(DEFAULT_COOKIE_NAME, "value")});
        assertTrue(repository.containsContext(requestMock));
    }

    @Test
    public void shouldReturnTrueWhenRequestWithSecurityCookieOfCustomNameGiven() {
        repository.setCookieName("security");
        when(requestMock.getCookies()).thenReturn(new Cookie[]{ new Cookie("security", "value")});
        assertTrue(repository.containsContext(requestMock));
    }
}
