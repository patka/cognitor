package org.cognitor.server.registration.web.controller;

import org.cognitor.server.account.web.controller.AccountController;
import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.cognitor.server.registration.web.controller.UserFormBean.ChangePasswordGroup;
import static org.cognitor.server.registration.web.controller.UserFormBean.PasswordGroup;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Patrick Kranz
 */
@Controller
public class ChangePasswordController {
    private static final String URL = "account/changePassword.html";
    private static final String PAGE = "changePassword";
    private static final String CURRENT_PASSWORD_WRONG_CODE = "changePassword.WrongCurrentPassword";
    private static final String CURRENT_PASSWORD_WRONG_DEFAULT_MESSAGE = "Current password is incorrect";
    private UserService userService;

    @Autowired
    public ChangePasswordController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = URL, method = GET)
    public ModelAndView showChangePasswordPage() {
        return new ModelAndView(PAGE, "userFormBean", new UserFormBean());
    }

    @RequestMapping(value = URL, method = POST)
    public ModelAndView changePassword(
            @Validated({ PasswordGroup.class, ChangePasswordGroup.class })
            UserFormBean formBean,
            BindingResult bindingResult,
            Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return createErrorView(bindingResult);
        }

        User user = mapFromFormBean(formBean);
        user.setEmail(authentication.getName());
        try {
            userService.changePassword(user, formBean.getCurrentPassword());
        } catch (BadCredentialsException exception) {
            bindingResult.addError(
            new FieldError(UserFormBean.class.getName(), "currentPassword",
                    null, false, new String[] { CURRENT_PASSWORD_WRONG_CODE },
                    null, CURRENT_PASSWORD_WRONG_DEFAULT_MESSAGE));
            return createErrorView(bindingResult);
        }
        return new ModelAndView("redirect:" + AccountController.ACCOUNT_URL);
    }

    private ModelAndView createErrorView(BindingResult bindingResult) {
        ModelAndView errorView = new ModelAndView(PAGE);
        errorView.addObject("errors", bindingResult.getFieldErrors());
        return errorView;
    }

    private User mapFromFormBean(UserFormBean formBean) {
        User user = new User();
        user.setPassword(formBean.getPassword());
        user.setEmail(formBean.getEmail());
        return user;
    }
}
