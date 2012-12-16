package org.cognitor.server.platform.web.filter;

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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class DurationFilterTest {

    @Mock
    private HttpServletRequest httpRequestMock;

    @Mock
    private HttpServletResponse httpResponseMock;

    @Mock
    private FilterChain filterChainMock;

    @Mock
    private Appender appenderMock;

    private DurationFilter filterToTest;

    @Before
    public void setup() {
        Logger.getRootLogger().addAppender(appenderMock);
        when(httpRequestMock.getRequestURL()).thenReturn(new StringBuffer("http://localhost"));
        filterToTest = new DurationFilter();
    }

    @After
    public void tearDown() {
        Logger.getRootLogger().removeAppender(appenderMock);
    }

    @Test
    public void shouldLogDurationMessageWhenHttpServletRequestGiven() throws Exception {
        // WHEN
        filterToTest.doFilter(httpRequestMock, httpResponseMock, filterChainMock);

        // THEN
        ArgumentCaptor<LoggingEvent> loggingEventArgumentCaptor = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(loggingEventArgumentCaptor.capture());

        LoggingEvent event = loggingEventArgumentCaptor.getValue();
        assertNotNull(event.getRenderedMessage());
        assertTrue(event.getRenderedMessage().startsWith("Serving of http://localhost took"));
        assertEquals(Level.DEBUG, event.getLevel());
    }

    @Test
    public void shouldLogWarningMessageWhenDurationAboveCriticalValueGiven() throws Exception {
        // GIVEN
        when(httpRequestMock.getRequestURL()).thenReturn(new StringBuffer("http://localhost"));
        // wait for a time > 80ms to simulate a slow request
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Thread.sleep(100);
                return true;
            }
        }).when(filterChainMock).doFilter(httpRequestMock, httpResponseMock);

        // WHEN
        filterToTest.doFilter(httpRequestMock, httpResponseMock, filterChainMock);

        // THEN
        ArgumentCaptor<LoggingEvent> loggingEventArgumentCaptor = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(appenderMock).doAppend(loggingEventArgumentCaptor.capture());

        LoggingEvent event = loggingEventArgumentCaptor.getValue();
        assertNotNull(event.getRenderedMessage());
        assertTrue(event.getRenderedMessage().startsWith("Serving of http://localhost took"));
        assertEquals(Level.WARN, event.getLevel());
    }

    @Test
    public void shouldContinueFilterChainWhenServletRequestGiven() throws Exception {
        // GIVEN
        ServletRequest servletRequestMock = mock(ServletRequest.class);
        ServletResponse servletResponseMock = mock(ServletResponse.class);
        // WHEN
        filterToTest.doFilter(servletRequestMock, servletResponseMock, filterChainMock);
        // THEN
        verify(filterChainMock, times(1)).doFilter(servletRequestMock, servletResponseMock);
    }

    @Test
    public void shouldContinueFilterChainWhenHttpServletRequestGiven() throws Exception {
        // WHEN
        filterToTest.doFilter(httpRequestMock, httpResponseMock, filterChainMock);
        // THEN
        verify(filterChainMock, times(1)).doFilter(httpRequestMock, httpResponseMock);
    }
}
