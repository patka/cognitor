package org.cognitor.server.registration.web.controller;

import org.cognitor.server.account.web.controller.AccountController;
import org.cognitor.server.platform.user.domain.User;
import org.cognitor.server.platform.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Patrick Kranz
 */
@Controller
public class ChangePasswordController {
    private static final String URL = "account/changePassword.html";
    private static final String PAGE = "changePassword";
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
            @Validated(UserFormBean.PasswordGroup.class) UserFormBean formBean,
            BindingResult bindingResult,
            Authentication authentication) {

        if (bindingResult.hasErrors()) {
            ModelAndView errorView = new ModelAndView(PAGE);
            errorView.addObject("errors", bindingResult.getFieldErrors());
            return errorView;
        }

        User user = mapFromFormBean(formBean);
        user.setEmail(authentication.getName());
        userService.changePassword(user);
        return new ModelAndView("redirect:" + AccountController.ACCOUNT_URL);
    }

    private User mapFromFormBean(UserFormBean formBean) {
        User user = new User();
        user.setPassword(formBean.getPassword());
        user.setEmail(formBean.getEmail());
        return user;
    }
}
