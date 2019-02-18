package me.lotabout.springsecurityexample.user;

import lombok.NonNull;
import me.lotabout.springsecurityexample.common.util.PasswordUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        switch (username) {
            case "admin":
                return org.springframework.security.core.userdetails.User.builder()
                        .username("admin")
                        .password(PasswordUtil.encode("admin"))
                        .authorities(Arrays.asList(Role.ADMIN, Role.USER))
                        .build();
            case "user":
                return org.springframework.security.core.userdetails.User.builder()
                        .username("user")
                        .password(PasswordUtil.encode("user"))
                        .authorities(Collections.singletonList(Role.USER))
                        .build();
        }

        throw new UsernameNotFoundException(username);
    }
}
