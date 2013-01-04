package org.cognitor.server.registration.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author Patrick Kranz
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @Mock
    HttpServletRequest requestMock;

    private LoginController controller;

    @Before
    public void setUp() {
        controller = new LoginController();
    }

    @Test
    public void shouldProvideModelWithActionUrlAndQueryParamsWhenQueryParamsGiven() {
        when(requestMock.getQueryString()).thenReturn("openid.mode=setup");

        ModelAndView modelAndView = controller.showLogin(requestMock);
        assertNotNull(modelAndView.getModel().get("actionUrl"));
        assertEquals("/login?openid.mode=setup", modelAndView.getModel().get("actionUrl"));
    }

    @Test
    public void shouldNotAppendQuestionMarkWhenNoQueryParametersGiven() {
        ModelAndView modelAndView = controller.showLogin(requestMock);

        assertNotNull(modelAndView.getModel().get("actionUrl"));
        assertEquals("/login", modelAndView.getModel().get("actionUrl"));
    }
}
