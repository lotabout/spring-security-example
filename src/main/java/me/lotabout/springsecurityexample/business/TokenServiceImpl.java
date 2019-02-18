package me.lotabout.springsecurityexample.business;

import lombok.NonNull;
import me.lotabout.springsecurityexample.common.MyError;
import me.lotabout.springsecurityexample.common.struct.ResultException;
import me.lotabout.springsecurityexample.security.TokenService;
import me.lotabout.springsecurityexample.user.Role;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public UserDetails authenticateToken(@NonNull String token) {
        if (token.equals("abcdefg")) {
            return User.builder()
                    .username("api")
                    .password("")
                    .authorities(Role.API)
                    .build();
        }

        throw ResultException.of(MyError.TOKEN_NOT_FOUND)
                .errorData(token);
    }
}
