package me.lotabout.springsecurityexample.security;

import org.springframework.stereotype.Component;

@Component
public class PathValidator {

    public boolean ensureHash(String user, String hash) {
        if (user == null || hash == null) {
            return false;
        }

        // put your validation logic here
        return user.equals(hash);
    }

}
