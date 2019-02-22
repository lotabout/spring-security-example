package me.lotabout.springsecurityexample.security;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class PathValidator {

    public boolean ensureHash(Authentication authentication, String user, String hash) {
        assert authentication instanceof AnonymousAuthenticationToken;
        if (user == null || hash == null) {
            return false;
        }

        // put your validation logic here
        return user.equals(hash);
    }

}
