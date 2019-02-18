package me.lotabout.springsecurityexample.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    USER,
    API;

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
