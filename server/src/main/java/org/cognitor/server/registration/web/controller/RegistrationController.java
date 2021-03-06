package org.cognitor.server.registration.web.controller;

import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.service.UserAlreadyExistsException;
import org.cognitor.server.platform.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.cognitor.server.platform.web.util.UrlUtil.appendQueryToUrl;
import static org.cognitor.server.registration.web.controller.LoginController.LOGIN_URL;

/**
 * @author Patrick Kranz
 */
@Controller
public class RegistrationController {

    public static final String EMAIL_EXISTS_ERROR_CODE = "Exists.email";
    public static final String REGISTRATION_URL = "/registration.html";

    private static final String REGISTRATION_PAGE = "registration";
    private static final String REGISTRATION_PAGE_URL_PARAM = "registrationPageUrl";
    private static final String EMAIL_EXISTS_DEFAULT_MESSAGE = "Email already in use";

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = REGISTRATION_URL)
    public ModelAndView enterPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(REGISTRATION_PAGE, "userFormBean", new UserFormBean());
        modelAndView.addObject(REGISTRATION_PAGE_URL_PARAM, getRegistrationPageUrl(request));
        return modelAndView;
    }

    @RequestMapping(value = REGISTRATION_URL, method = RequestMethod.POST)
    public ModelAndView registerUser(@Validated({ UserFormBean.PasswordGroup.class, UserFormBean.EmailGroup.class })
                                     @ModelAttribute UserFormBean userFormBean,
                                     BindingResult bindingResult,
                                     HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return createErrorView(bindingResult.getFieldErrors(), request);
        }
        try {
            userService.registerUser(getUserFromBean(userFormBean));
        } catch (UserAlreadyExistsException exception) {
            bindingResult.addError(createEmailExistsError(userFormBean.getEmail()));
            return createErrorView(bindingResult.getFieldErrors(), request);
        }

        ModelAndView modelAndView = new ModelAndView("registrationSuccess");
        modelAndView.addObject("loginUrl", getLoginUrl(request));
        return modelAndView;
    }

    private static FieldError createEmailExistsError(String email) {
        return new FieldError(UserFormBean.class.getName(), "email",
            email, false, new String[] { EMAIL_EXISTS_ERROR_CODE },
            null, EMAIL_EXISTS_DEFAULT_MESSAGE);
    }

    private static User getUserFromBean(UserFormBean formBean) {
        return new User(formBean.getEmail(), formBean.getPassword());
    }

    private static ModelAndView createErrorView(List<FieldError> errors, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(REGISTRATION_PAGE);
        modelAndView.addObject(REGISTRATION_PAGE_URL_PARAM, getRegistrationPageUrl(request));
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
