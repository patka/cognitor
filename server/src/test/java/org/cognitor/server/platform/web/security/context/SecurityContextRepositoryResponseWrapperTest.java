package org.cognitor.server.platform.web.security.context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class SecurityContextRepositoryResponseWrapperTest {
    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private SecurityContextRepository repositoryMock;

    @Test(expected = IllegalArgumentException.class)
    public void ShouldThrowExceptionWhenNoRequestForConstructorGiven() {
        new SecurityContextRepositoryResponseWrapper(null, responseMock, repositoryMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ShouldThrowExceptionWhenNoResponseForConstructorGiven() {
        new SecurityContextRepositoryResponseWrapper(requestMock, null, repositoryMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ShouldThrowExceptionWhenNoRepositoryForConstructorGiven() {
        new SecurityContextRepositoryResponseWrapper(requestMock, responseMock, null);
    }

    @Test
    public void ShouldSaveSecurityContextWhenUncommittedResponseGiven() {
        // GIVEN
        SecurityContextRepositoryResponseWrapper wrapper =
                new SecurityContextRepositoryResponseWrapper(requestMock, responseMock, repositoryMock);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        when(responseMock.isCommitted()).thenReturn(false);
        // WHEN
        wrapper.saveContext(securityContextMock);
        // THEN
        verify(repositoryMock, atLeastOnce()).saveContext(securityContextMock, requestMock, responseMock);
    }

    @Test
    public void ShouldNotSaveSecurityContextWhenCommittedResponseGiven() {
        // GIVEN
        SecurityContextRepositoryResponseWrapper wrapper =
                new SecurityContextRepositoryResponseWrapper(requestMock, responseMock, repositoryMock);
        SecurityContext securityContextMock = Mockito.mock(SecurityContext.class);
        when(responseMock.isCommitted()).thenReturn(true);
        // WHEN
        wrapper.saveContext(securityContextMock);
        // THEN
        verify(repositoryMock, never()).saveContext(securityContextMock, requestMock, responseMock);
    }
}
