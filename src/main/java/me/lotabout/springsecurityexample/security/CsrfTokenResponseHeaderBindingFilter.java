package me.lotabout.springsecurityexample.security;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CsrfTokenResponseHeaderBindingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    javax.servlet.FilterChain filterChain) throws ServletException, IOException {
        CSRFUtil.writeCSRFToHeader(request, response);
        filterChain.doFilter(request, response);
    }

}
