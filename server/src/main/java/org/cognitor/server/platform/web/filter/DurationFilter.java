package org.cognitor.server.platform.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Patrick Kranz
 */
public class DurationFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DurationFilter.class);
    private static final int CRITICAL_DURATION = 80;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            filterChain.doFilter(servletRequest, servletResponse);
            long duration = System.currentTimeMillis() - startTime;
            logDuration(httpServletRequest, duration);            
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void logDuration(HttpServletRequest request, long duration) {
        if (duration < CRITICAL_DURATION) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(createLogMessage(request.getRequestURL().toString(), duration));
            }
        } else {
            LOGGER.warn(createLogMessage(request.getRequestURL().toString(), duration));
        }
    }
    
    private String createLogMessage(String url, long duration) {
        StringBuilder message = new StringBuilder();
        message.append("Serving of ");
        message.append(url);
        message.append(" took ").append(duration).append(" ms.");
        return message.toString();
    }
    
    @Override
    public void destroy() {
    }
}
