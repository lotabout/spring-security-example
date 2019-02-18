package me.lotabout.springsecurityexample.security;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CSRFUtil {

    public static final String CSRF_ATTRIBUTE_NAME = "_csrf";
    protected static final String CSRF_HEADER_NAME = "X-XSRF-HEADER";
    protected static final String CSRF_PARAM_NAME = "X-XSRF-PARAM";
    public static final String CSRF_TOKEN_NAME = "X-XSRF-TOKEN";
    public static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";

    public static void writeCSRFToHeader(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken token = (CsrfToken) request.getAttribute(CSRF_ATTRIBUTE_NAME);

        if (token != null) {
            response.setHeader(CSRF_HEADER_NAME, token.getHeaderName());
            response.setHeader(CSRF_PARAM_NAME, token.getParameterName());
            response.setHeader(CSRF_TOKEN_NAME, token.getToken());
        }
    }

    public static void writeCSRFToCookie(HttpServletRequest request, HttpServletResponse response) {
        CsrfToken token = (CsrfToken) request.getAttribute(CSRF_ATTRIBUTE_NAME);
        if (token != null) {
            Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
            setCookie(response, cookie, CSRF_COOKIE_NAME, token.getToken());
        }
    }

    private static void setCookie(
            HttpServletResponse response,
            Cookie cookie,
            String key,
            String value) {
        if (cookie == null || value != null && !value.equals(cookie.getValue())) {
            cookie = new Cookie(key, value);
            cookie.setPath("/");
            cookie.setHttpOnly(false);
            response.addCookie(cookie);
        }
    }
}
