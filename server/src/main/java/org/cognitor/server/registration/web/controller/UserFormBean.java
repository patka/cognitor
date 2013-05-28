package org.cognitor.server.registration.web.controller;

import org.cognitor.server.platform.validation.FieldEqual;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Patrick Kranz
 */
@FieldEqual(field = "password", verificationField = "passwordVerification", groups = UserFormBean.PasswordGroup.class)
public class UserFormBean {

    @NotEmpty(groups = EmailGroup.class)
    @Email(groups = EmailGroup.class)
    private String email;

    @Length(min = 6, max = 100, groups = PasswordGroup.class)
    private String password;

    @NotEmpty(groups = PasswordGroup.class)
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

    public interface PasswordGroup {}
    public interface EmailGroup {}
}
