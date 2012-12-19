package org.cognitor.server.registration.web.controller;

import org.cognitor.server.platform.validation.FieldEqual;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;

/**
 * User: patrick
 * Date: 12.12.12
 */
@FieldEqual(field = "password", verificationField = "passwordVerification")
public class RegistrationFormBean {

    @NotEmpty
    @Email
    private String email;

    @Length(min = 6, max = 100)
    private String password;

    @NotEmpty
    private String passwordVerification;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordVerification() {
        return passwordVerification;
    }

    public void setPasswordVerification(String passwordVerification) {
        this.passwordVerification = passwordVerification;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
