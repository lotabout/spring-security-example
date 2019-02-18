package me.lotabout.springsecurityexample.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.lotabout.springsecurityexample.common.util.JsonUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        AuthReq authReq;
        if ("application/json".equals(request.getHeader("Content-Type"))) {
            authReq = getAuthFromJson(request);
        } else {
            authReq = getAuthFromFormData(request);
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getPassword());
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    private AuthReq getAuthFromJson(HttpServletRequest request) {
        AuthReq authReq;

        try (Reader reader = request.getReader()) {
            authReq = JsonUtil.getObjectMapper().readValue(reader, AuthReq.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("failed to get username or password from request");
        }

        return authReq;
    }

    private AuthReq getAuthFromFormData(HttpServletRequest request) {
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        return new AuthReq(username.trim(), password);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AuthReq {
        private String username;
        private String password;
    }
}
