/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.cognitor.server.registration.web.controller;

import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cognitor.server.platform.web.util.UrlUtil.appendQueryToUrl;
import static org.cognitor.server.registration.web.controller.LoginController.LOGIN_URL;

/**
 * @author patrick
 */
@Controller
public class RegistrationController {

    public static final String REGISTRATION_URL = "/registration.html";

    private static final String REGISTRATION_PAGE = "registration";

    private UserService userService;
    
    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = REGISTRATION_URL)
    public ModelAndView enterPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(REGISTRATION_PAGE);
        modelAndView.addObject("registrationPageUrl", getRegistrationPageUrl(request));
        return modelAndView;
    }

    @RequestMapping(value = REGISTRATION_URL, method = RequestMethod.POST)
    public ModelAndView registerUser(@Valid @ModelAttribute RegistrationFormBean formBean,
                                     BindingResult bindingResult,
                                     HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return createErrorView(bindingResult.getFieldErrors(), request);
        }
        try {
            userService.registerUser(getUserFromBean(formBean));
        } catch (IllegalStateException ise) {
            FieldError error = new FieldError("User", "email",
                    formBean.getEmail(), false, new String[] { "Exists.email" }, null, "User already exists.");
            bindingResult.addError(error);
            return createErrorView(bindingResult.getFieldErrors(), request);
        }
        ModelAndView modelAndView = new ModelAndView("registrationSuccess");
        modelAndView.addObject("loginUrl", getLoginUrl(request));
        return modelAndView;
    }

    private static User getUserFromBean(RegistrationFormBean formBean) {
        return new User(formBean.getEmail(), formBean.getPassword());
    }

    @ModelAttribute
    private static RegistrationFormBean createNewUser() {
        return new RegistrationFormBean();
    }
    
    private static ModelAndView createErrorView(List<FieldError> errors, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(REGISTRATION_PAGE);
        modelAndView.addObject("registrationPageUrl", getRegistrationPageUrl(request));
        modelAndView.addObject("errors", errors);
        return modelAndView;
    }

    private static String getLoginUrl(HttpServletRequest request) {
        return appendQueryToUrl(LOGIN_URL, request.getQueryString());
    }

    private static String getRegistrationPageUrl(HttpServletRequest request) {
        return appendQueryToUrl(REGISTRATION_URL, request.getQueryString());
    }
}
