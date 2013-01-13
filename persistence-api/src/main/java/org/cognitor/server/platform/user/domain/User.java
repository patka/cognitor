package org.cognitor.server.platform.user.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Patrick Kranz
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @Email
    @NotNull
    private String email;

    @Length(min = 6, max = 100)
    @NotNull
    private String password;

    @SuppressWarnings("unused")
    public User() {
    }
    
    public User(String username, String password) {
        this.email = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("org.cognitor.server.platform.user.domain.User[id=");
        builder.append(id);
        builder.append("; email=");
        builder.append(email);
        builder.append("]");
        return builder.toString();
    }

}
