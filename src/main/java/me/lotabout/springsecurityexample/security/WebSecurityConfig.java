package me.lotabout.springsecurityexample.security;

import me.lotabout.springsecurityexample.common.util.PasswordUtil;
import me.lotabout.springsecurityexample.user.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.LazyCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private CustomUserDetailService customUserDetailService;

    private static final String LOGIN_URL = "/api/v1/user/login";
    private static final String LOGOUT_URL = "/api/v1/user/logout";

    private static final String PATTERN_SQUARE = "/api/v1/square/**";
    private static final String PATTERN_TRIPLE = "/api/v1/triple/**";
    private static final String PATTERN_ADMIN = "/api/v1/admin";
    private static final String PATTERN_IGNORE = "/api/v1/ignore";

    /**
     * User token based authentication for predictor API, also disable session
     */
    @Configuration
    @Order(1)
    class PredictorSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(new TokenAuthenticationProvider(tokenService));
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher(PATTERN_SQUARE)
                    .addFilterAfter(new TokenAuthenticationFilter(),
                            BasicAuthenticationFilter.class)
                    .addFilterAfter(new ResultExceptionTranslationFilter(),
                            ExceptionTranslationFilter.class)
                    .authorizeRequests()
                    .anyRequest().hasRole("API")
                    .and()
                    .csrf()
                    .disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    /**
     * Authentication based on parameter URLs
     */
    @Configuration
    @Order(2)
    class PathTokenSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher(PATTERN_TRIPLE)
                    .authorizeRequests()
                    .antMatchers("/api/v1/triple/{user}/{hash}/**")
                    .access("@pathValidator.ensureHash(#user, #hash)")
                    .and()
                    .csrf()
                    .disable() // need to disable csrf
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }

    @Configuration
    @Order(3)
    class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(customUserDetailService)
                    .passwordEncoder(PasswordUtil.getEncoder());
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(PATTERN_IGNORE);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .addFilterAfter(new CsrfTokenResponseHeaderBindingFilter(), CsrfFilter.class)
                    .addFilterBefore(authenticationFilter(),
                            UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                    .and()
                    .authorizeRequests()
                    .antMatchers(PATTERN_ADMIN).hasRole("ADMIN")
                    .antMatchers("/**").hasRole("USER")
                    .and()
                    .csrf()
                    .csrfTokenRepository(csrfTokenRepository())
                    .ignoringAntMatchers(LOGIN_URL, LOGOUT_URL)
                    .and()
                    .logout()
                    .logoutUrl(LOGOUT_URL)
                    .permitAll()
                    .logoutSuccessHandler(new AjaxLogoutSuccessHandler());
        }

        private CsrfTokenRepository csrfTokenRepository() {
            // reset the default header name to X-XSRF-TOKEN, was X-CSRF-TOKEN
            // so as to work with front-end libraries.
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName(CSRFUtil.CSRF_TOKEN_NAME);
            repository.setParameterName(CSRFUtil.CSRF_ATTRIBUTE_NAME);
            return new LazyCsrfTokenRepository(repository);
        }

        private JsonAuthenticationFilter authenticationFilter() throws Exception {
            JsonAuthenticationFilter filter = new JsonAuthenticationFilter();
            filter.setAuthenticationManager(authenticationManager());
            filter.setAuthenticationSuccessHandler(new AjaxAuthenticationSuccessHandler());
            filter.setAuthenticationFailureHandler(new AjaxAuthenticationFailureHandler());
            filter.setFilterProcessesUrl(LOGIN_URL);
            return filter;
        }
    }
}
