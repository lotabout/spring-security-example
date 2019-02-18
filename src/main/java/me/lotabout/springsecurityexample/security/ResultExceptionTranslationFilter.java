package me.lotabout.springsecurityexample.security;

import me.lotabout.springsecurityexample.common.struct.Response;
import me.lotabout.springsecurityexample.common.struct.ResultException;
import me.lotabout.springsecurityexample.common.util.JsonUtil;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * There might be ResultExceptions during authentication, this filter will transfer the exceptions
 * into 200 with JSON body explaining why.
 * <p>
 * Mainly used for Token Authentication.
 */

public class ResultExceptionTranslationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        try {
            fc.doFilter(request, response);
        } catch (ResultException ex) {
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().println(JsonUtil.toJson(Response.of(ex)));
            response.getWriter().flush();
        }
    }
}
