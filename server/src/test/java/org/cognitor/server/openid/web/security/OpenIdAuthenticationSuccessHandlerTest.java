package org.cognitor.server.openid.web.security;

import org.cognitor.server.openid.web.OpenIdManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class OpenIdAuthenticationSuccessHandlerTest {

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private Authentication authenticationMock;

    @Mock
    private OpenIdManager openIdManager;


    @Test
    public void shouldSendRedirectWhenSuccessfulLoginGiven() throws IOException, ServletException {
        OpenIdAuthenticationSuccessHandler handler = new OpenIdAuthenticationSuccessHandler(openIdManager);
        when(openIdManager.isOpenIdRequest(requestMock)).thenReturn(true);
        when(authenticationMock.getName()).thenReturn("username");
        when(authenticationMock.isAuthenticated()).thenReturn(true);
        when(openIdManager.getAuthenticationResponseReturnToUrl(requestMock, "username", true)).thenReturn(
                "http://return.url");

        handler.onAuthenticationSuccess(requestMock, responseMock, authenticationMock);

        verify(responseMock, times(1)).sendRedirect("http://return.url");
    }
}
