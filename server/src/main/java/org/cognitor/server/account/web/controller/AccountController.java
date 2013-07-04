package org.cognitor.server.account.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Patrick Kranz
 */
@Controller
public class AccountController {
    private static final String ACCOUNT_PAGE = "manageAccount";
    private static final String ACCOUNT_URL = "/account";

    @RequestMapping(value = ACCOUNT_URL, method= GET)
    public String getManageAccountPage() {
        return ACCOUNT_PAGE;
    }
}
