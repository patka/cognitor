package org.cognitor.server.platform.security;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.util.Assert.notNull;

/**
 * @author Patrick Kranz
 */
public class UserDetailsImpl implements UniqueKeyUserDetails, CredentialsContainer {
    public static final String ROLE_USER = "ROLE_USER";
    private String username;
    private String password;
    private String uniqueKey;

    public UserDetailsImpl(String username, String password, String uniqueKey) {
        notNull(username);
        notNull(uniqueKey);
        this.username = username;
        this.password = password;
        this.uniqueKey = uniqueKey;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(1);
        authorities.add(new SimpleGrantedAuthority(ROLE_USER));
        return authorities;
    }

    @Override
    public String getUniqueKey() {
        return uniqueKey;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }
}