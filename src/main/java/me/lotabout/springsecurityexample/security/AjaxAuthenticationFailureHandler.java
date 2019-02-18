package me.lotabout.springsecurityexample.security;

import me.lotabout.springsecurityexample.common.MyError;
import me.lotabout.springsecurityexample.common.struct.Response;
import me.lotabout.springsecurityexample.common.util.JsonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * We want to return 200 when failure where the body contains {@code "success": false}
 * and the detailed error. Without custom failure handler, it just return 401
 */
public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Response error;
        if (exception instanceof DisabledException) {
            error = Response.error(MyError.LOGIN_ACCOUNT_DISABLED);
            response.addHeader("X-inactive-username", "true");
        } else if (exception instanceof BadCredentialsException) {
            error = Response.error(MyError.LOGIN_BAD_CREDENTIAL);
            response.addHeader("X-invalid-username-password", "true");
        } else {
            error = Response.error(MyError.LOGIN_ERROR);
        }

        response.getWriter().println(JsonUtil.toJson(error.description(exception.getMessage())));
        response.getWriter().flush();
    }
}
