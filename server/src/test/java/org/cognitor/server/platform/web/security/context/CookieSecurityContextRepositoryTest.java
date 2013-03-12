package org.cognitor.server.platform.web.security.context;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.cognitor.server.platform.web.security.context.CookieSecurityContextRepository.DEFAULT_COOKIE_NAME;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class CookieSecurityContextRepositoryTest {
    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private SecurityCookieMarshaller securityCookieMarshaller;

    private HttpRequestResponseHolder holder;
    private CookieSecurityContextRepository repository;

    @Before
    public void setUp() {
        holder = new HttpRequestResponseHolder(requestMock, responseMock);
        this.repository = new CookieSecurityContextRepository(securityCookieMarshaller);
    }

    @After
    public void tearDown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullValueForCookieNameGiven() {
        repository.setCookieName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenEmptyStringValueForCookieNameGiven() {
        repository.setCookieName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullAsCookiePathGiven() {
        repository.setCookiePath(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenEmptyStringAsCookiePathGiven() {
        repository.setCookiePath("");
    }

    // Contains method
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

    // Constructor
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullValueForSerializerGiven() {
        new CookieSecurityContextRepository(null);
    }

    // load context
    @Test
    public void shouldWrapResponseWhenHttpRequestResponseHolderGiven() {
        repository.loadContext(holder);
        assertTrue(SaveContextOnUpdateOrErrorResponseWrapper.class.isAssignableFrom(holder.getResponse().getClass()));
    }

    @Test
    public void shouldReturnEmptySecurityContextWhenNoSecurityCookieGiven() {
        SecurityContext context = repository.loadContext(holder);
        assertNotNull(context);
        assertNull(context.getAuthentication());
    }

    @Test
    public void shouldReturnEmptySecurityContextWhenDeserializingReturnsNull() {
        when(requestMock.getCookies()).thenReturn(new Cookie[] { new Cookie("context", "") });
        when(securityCookieMarshaller.getSecurityCookie(anyString())).thenReturn(null);
        SecurityContext context = repository.loadContext(holder);
        assertNotNull(context);
        assertNull(context.getAuthentication());
    }

    @Test
    public void shouldReturnEmptySecurityContextWhenSecurityCookieIsExpired() {
        SecurityCookie expiredCookie = new SecurityCookie(new SecurityContextImpl(), DateTime.now().minusMinutes(1));
        when(requestMock.getCookies()).thenReturn(new Cookie[] { new Cookie("context", "") });
        when(securityCookieMarshaller.getSecurityCookie(anyString())).thenReturn(expiredCookie);
        SecurityContext context = repository.loadContext(holder);
        assertNotNull(context);
        assertNull(context.getAuthentication());
    }

    @Test
    public void shouldReturnDeserializedContextWhenValidCookieGiven() {
        final SecurityContext validContext = new SecurityContextImpl();
        final SecurityCookie validCookie = new SecurityCookie(validContext, DateTime.now().plusMinutes(1));
        when(requestMock.getCookies()).thenReturn(new Cookie[] { new Cookie("context", "") });
        when(securityCookieMarshaller.getSecurityCookie(anyString())).thenReturn(validCookie);
        SecurityContext context = repository.loadContext(holder);
        assertEquals(validContext, context);
    }

    @Test
    public void shouldAttachSecurityCookieToResponseWhenSecurityContextGiven() {
        when(securityCookieMarshaller.getBase64EncodedValue(any(SecurityCookie.class))).thenReturn("data");
        ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor.forClass(Cookie.class);
        repository.saveContext(createNonAnonymousSecurityContext(), requestMock, responseMock);
        verify(responseMock, atLeastOnce()).addCookie(cookieArgumentCaptor.capture());
        Cookie securityCookie = cookieArgumentCaptor.getValue();
        assertEquals(DEFAULT_COOKIE_NAME, securityCookie.getName());
        assertEquals("data", securityCookie.getValue());
        assertEquals(1800, securityCookie.getMaxAge());
    }

    @Test
    public void shouldAttachSecurityCookieWithConfiguredParamteresWhenConfigurationOptionsGiven() {
        repository.setCookieDomain("testdomain.de");
        repository.setCookiePath("/relativePath");
        repository.setCookieSecure(true);
        when(securityCookieMarshaller.getBase64EncodedValue(any(SecurityCookie.class))).thenReturn("data");
        ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor.forClass(Cookie.class);
        repository.saveContext(createNonAnonymousSecurityContext(), requestMock, responseMock);
        verify(responseMock, atLeastOnce()).addCookie(cookieArgumentCaptor.capture());
        Cookie securityCookie = cookieArgumentCaptor.getValue();
        assertEquals("testdomain.de", securityCookie.getDomain());
        assertEquals("/relativePath", securityCookie.getPath());
        assertTrue(securityCookie.getSecure());
    }


    @Test
    public void shouldNotAttachCookieToResponseWhenEmptySerializedStringGiven() {
        SecurityContextImpl securityContext = new SecurityContextImpl();
        when(securityCookieMarshaller.getBase64EncodedValue(any(SecurityCookie.class))).thenReturn("");
        repository.saveContext(securityContext, requestMock, responseMock);

        verify(responseMock, never()).addCookie(any(Cookie.class));
    }

    @Test
    public void shouldNotAttachCookieToResponseWhenNullAsSerializedStringGiven() {
        SecurityContextImpl securityContext = new SecurityContextImpl();
        when(securityCookieMarshaller.getBase64EncodedValue(any(SecurityCookie.class))).thenReturn(null);
        repository.saveContext(securityContext, requestMock, responseMock);

        verify(responseMock, never()).addCookie(any(Cookie.class));
    }

    @Test
    public void shouldNotAttachCookieToResponseWhenCommittedResponseGiven() {
        SecurityContext securityContext = new SecurityContextImpl();
        when(responseMock.isCommitted()).thenReturn(true);
        repository.saveContext(securityContext, requestMock, responseMock);
        verify(securityCookieMarshaller, never()).getBase64EncodedValue(any(SecurityCookie.class));
    }

    @Test
    public void shouldNotAttachCookieToResponseWhenAnonymousAuthenticationGiven() {
        SecurityContext securityContext = new SecurityContextImpl();
        when(responseMock.isCommitted()).thenReturn(false);
        repository.saveContext(securityContext, requestMock, responseMock);
        verify(securityCookieMarshaller, never()).getBase64EncodedValue(any(SecurityCookie.class));
    }

    @Test
    public void shouldAddSessionValidUntilDateWhenSecurityContextGiven() {
        // GIVEN
        DateTimeUtils.setCurrentMillisFixed(5000);
        ArgumentCaptor<SecurityCookie> cookieArgumentCaptor = ArgumentCaptor.forClass(SecurityCookie.class);
        when(securityCookieMarshaller.getBase64EncodedValue(any(SecurityCookie.class))).thenReturn("data");

        // WHEN
        repository.saveContext(createNonAnonymousSecurityContext(), requestMock, responseMock);

        // THEN
        verify(securityCookieMarshaller, atLeastOnce()).getBase64EncodedValue(cookieArgumentCaptor.capture());
        assertEquals(new DateTime(1805000), cookieArgumentCaptor.getValue().getValidUntil());
    }

    @Test
    public void shouldUseConfiguredValidUntilWhenSessionDurationAndSecurityContextGiven() {
        // GIVEN
        repository.setSessionDurationSeconds(10);
        DateTimeUtils.setCurrentMillisFixed(5000);
        ArgumentCaptor<SecurityCookie> cookieArgumentCaptor = ArgumentCaptor.forClass(SecurityCookie.class);
        when(securityCookieMarshaller.getBase64EncodedValue(any(SecurityCookie.class))).thenReturn("data");

        // WHEN
        repository.saveContext(createNonAnonymousSecurityContext(), requestMock, responseMock);

        // THEN
        verify(securityCookieMarshaller, atLeastOnce()).getBase64EncodedValue(cookieArgumentCaptor.capture());
        assertEquals(new DateTime(15000), cookieArgumentCaptor.getValue().getValidUntil());
    }

    @Test
    public void shouldRenewSecurityCookieWhenValidContextGiven() {
        DateTimeUtils.setCurrentMillisFixed(3000);
        SecurityContext shouldBeReturned = createNonAnonymousSecurityContext();
        SecurityCookie securityCookie = new SecurityCookie(shouldBeReturned, DateTime.now().plusMinutes(1));
        when(securityCookieMarshaller.getSecurityCookie(anyString())).thenReturn(securityCookie);

        when(requestMock.getCookies()).thenReturn(new Cookie[]{new Cookie("context", "")});
        when(responseMock.isCommitted()).thenReturn(false);
        when(securityCookieMarshaller.getBase64EncodedValue(any(SecurityCookie.class))).thenReturn("bla");
        ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor.forClass(Cookie.class);

        repository.loadContext(holder);

        verify(responseMock, atLeastOnce()).addCookie(cookieArgumentCaptor.capture());
        Cookie cookie = cookieArgumentCaptor.getValue();
        assertEquals("bla",cookie.getValue());
        assertEquals(1800, cookie.getMaxAge());
    }

    private static SecurityContext createNonAnonymousSecurityContext() {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken("username", "credentials");
        context.setAuthentication(authenticationToken);
        return context;
    }
}
