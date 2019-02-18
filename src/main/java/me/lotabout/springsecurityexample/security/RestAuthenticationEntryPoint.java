package me.lotabout.springsecurityexample.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * By default, If a request is not authenticated, Spring security will send 302 and redirect it to
 * the login page. It does not make sense in REST services where unauthenticated requests should
 * fail with 401.
 *
 * Ref: https://www.baeldung.com/securing-a-restful-web-service-with-spring-security
 */

public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}

