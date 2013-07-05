package org.cognitor.server.registration.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.cognitor.server.platform.web.util.UrlUtil.appendQueryToUrl;
import static org.cognitor.server.registration.web.controller.RegistrationController.REGISTRATION_URL;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author Patrick Kranz
 */
@Controller
public class LoginController {
    public static final String LOGIN_URL = "/";
    public static final String LOGIN_FAILED_URL = "/loginFailed.html";

    private String securityChainUrl = "/login";

    @RequestMapping(value = LOGIN_URL, method = RequestMethod.GET)
    public ModelAndView showLogin(HttpServletRequest request) {
        Map<String, String> model = new HashMap<>();
        model.put("actionUrl", getLoginActionUrl(request));
        model.put("registrationPageUrl", getRegistrationPageUrl(request));
        return new ModelAndView("login", model);
    }

    @RequestMapping(value = LOGIN_FAILED_URL, method = RequestMethod.GET)
    public ModelAndView loginFailed(HttpServletRequest request) {
        ModelAndView model = showLogin(request);
        model.addObject("error", "login.badCredentials");
        return model;
    }

    public void setSecurityChainUrl(String url) {
        if (hasText(url)) {
            this.securityChainUrl = url;
        }
    }

    private String getLoginActionUrl(HttpServletRequest request) {
        return appendQueryToUrl(securityChainUrl, request.getQueryString());
    }

    private static String getRegistrationPageUrl(HttpServletRequest request) {
        return appendQueryToUrl(REGISTRATION_URL, request.getQueryString());
    }
}
