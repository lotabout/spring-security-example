package me.lotabout.springsecurityexample.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {
    UserDetails authenticateToken(String token);
}
