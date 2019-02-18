package me.lotabout.springsecurityexample.common.util;

import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

    public static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encode(@NonNull String password) {
        return encoder.encode(password);
    }

    public static boolean matches(@NonNull String rawPassword, @NonNull String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

    public static PasswordEncoder getEncoder() {
        return encoder;
    }

}
