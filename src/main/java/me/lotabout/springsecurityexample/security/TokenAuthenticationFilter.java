package me.lotabout.springsecurityexample.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * Grab the `?token=xxx` parameter into authentication, so as to trigger spring security's
 * authentication
 */

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain fc)
            throws ServletException, IOException {

        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null && context.getAuthentication().isAuthenticated()) {
            // do nothing
        } else {
            Map<String, String[]> params = req.getParameterMap();
            if (!params.isEmpty() && params.containsKey("token")) {
                String token = params.get("token")[0];
                if (token != null) {
                    Authentication auth = new TokenAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            req.setAttribute("me.lotabout.springsecurityexample.security.TokenAuthenticationFilter.FILTERED", true);
        }

        fc.doFilter(req, res);
    }

    class TokenAuthentication implements Authentication {
        private String token;

        private TokenAuthentication(String token) {
            this.token = token;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return null;
        }

        @Override
        public Object getCredentials() {
            return token;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        }

        @Override
        public String getName() {
            return "token";
        }
    }
}
