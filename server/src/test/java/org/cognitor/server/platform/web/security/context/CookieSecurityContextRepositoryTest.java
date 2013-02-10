package org.cognitor.server.platform.web.security.context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.codec.Base64;
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
    private SecurityContextSerializer cookieSerializerMock;

    private HttpRequestResponseHolder holder;
    private CookieSecurityContextRepository repository;

    @Before
    public void setUp() {
        holder = new HttpRequestResponseHolder(requestMock, responseMock);
        this.repository = new CookieSecurityContextRepository(cookieSerializerMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullValueForCookieNameGiven() {
        repository.setCookieName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenEmptyStringValueForCookieNameGiven() {
        repository.setCookieName("");
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
    public void shouldReturnEmptySecurityContextWhenDeserializingThrowsException() {
        when(requestMock.getCookies()).thenReturn(new Cookie[] { new Cookie("context", "") });
        when(cookieSerializerMock.deserialize(any(byte[].class))).thenThrow(new SerializeException("test"));
        SecurityContext context = repository.loadContext(holder);
        assertNotNull(context);
        assertNull(context.getAuthentication());
    }

    @Test
    public void shouldReturnEmptySecurityContextWhenNoBase64EncodedCookieContentGiven() {
        when(requestMock.getCookies()).thenReturn(new Cookie[] { new Cookie("context", "") });
        when(cookieSerializerMock.deserialize(any(byte[].class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                byte[] serializedContent = (byte[]) invocation.getArguments()[0];
                if (serializedContent.length == 0) {
                    throw new SerializeException("no input data");
                } else {
                    fail("expected empty byte[]");
                    return null;
                }
            }
        });
        SecurityContext context = repository.loadContext(holder);
        assertNotNull(context);
        assertNull(context.getAuthentication());
    }

    @Test
    public void shouldReturnDeserializedContextWhenValidBase64EncodedDataGiven() {
        final SecurityContext validContext = new SecurityContextImpl();
        byte[] validData = Base64.encode("hello".getBytes());
        when(cookieSerializerMock.deserialize(any(byte[].class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                byte[] serializedContent = (byte[]) invocation.getArguments()[0];
                String value = new String(serializedContent);
                if ("hello".equals(value)) {
                    return validContext;
                } else {
                    fail("should have been a valid context");
                    return null;
                }
            }
        });
        when(requestMock.getCookies()).thenReturn(new Cookie[]{
                new Cookie("context", new String(validData))});

        SecurityContext result = repository.loadContext(holder);
        assertEquals(validContext, result);
    }

    // save context
    @Test
    public void shouldAttachSecurityCookieToResponseWhenSecurityContextGiven() {
        when(cookieSerializerMock.serialize(any(SecurityContext.class))).thenReturn(new byte[] { 1, 2, 3 });
        ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor.forClass(Cookie.class);
        repository.saveContext(new SecurityContextImpl(), requestMock, responseMock);
        verify(responseMock, atLeastOnce()).addCookie(cookieArgumentCaptor.capture());
        Cookie securityCookie = cookieArgumentCaptor.getValue();
        assertEquals(DEFAULT_COOKIE_NAME, securityCookie.getName());
        assertNotNull(securityCookie.getValue());
    }

    @Test
    public void shouldEncodeDataBase64WhenSecurityContextGiven() {
        final byte[] serializedContext = {1, 2, 3};
        when(cookieSerializerMock.serialize(any(SecurityContext.class))).thenReturn(serializedContext);
        ArgumentCaptor<Cookie> cookieArgumentCaptor = ArgumentCaptor.forClass(Cookie.class);
        repository.saveContext(new SecurityContextImpl(), requestMock, responseMock);
        verify(responseMock, atLeastOnce()).addCookie(cookieArgumentCaptor.capture());
        String cookieContent = cookieArgumentCaptor.getValue().getValue();
        byte[] cookieDecoded = Base64.decode(cookieContent.getBytes());
        assertArrayEquals(serializedContext, cookieDecoded);
    }

    @Test
    public void shouldNotAttachCookieToResponseWhenSerializationExceptionGiven() {
        SecurityContextImpl securityContext = new SecurityContextImpl();
        when(cookieSerializerMock.serialize(securityContext))
                .thenThrow(new SerializeException("test"));
        repository.saveContext(securityContext, requestMock, responseMock);

        verify(responseMock, never()).addCookie(any(Cookie.class));
    }
}
