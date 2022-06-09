package com.home.utilities.configuration.security;

import com.home.keycode.filters.KeyCodeFilter;
import com.home.utilities.configuration.security.authentication.AuthFailureHandler;
import com.home.utilities.configuration.security.authentication.AuthSuccessHandler;
import com.home.utilities.configuration.security.filters.AuthenticationFilter;
import com.home.utilities.configuration.security.filters.RedirectPageFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
      securedEnabled = true,
      jsr250Enabled = true,
      prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] VIEW_MAPPING = {"/", "/#", "/register", "/login/**", "/images/keycode", "/register/account/activate/**", "/terms-and-conditions", "/privacy-policy", "/support/**", "/contact"};
    private static final String[] STATIC_CONTENT = {"/appIcon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js"};

    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final KeyCodeFilter keyCodeFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationFailureHandler authFailureHandler() {
        return new AuthFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler() {
        return new AuthSuccessHandler();
    }

    @Bean
    public GenericFilterBean redirectPageFilter() {
        return new RedirectPageFilter();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
              .userDetailsService(userDetailsService)
              .passwordEncoder(passwordEncoder());
    }

    @Bean
    public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
        final var filter = new AuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationFailureHandler(authFailureHandler());
        filter.setAuthenticationSuccessHandler(authSuccessHandler());
        return filter;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
              // Enable CORS
              .cors()
              .and()

              // Set session management
              .sessionManagement().maximumSessions(1)
              .and()
              .sessionCreationPolicy(SessionCreationPolicy.NEVER)
              .and()

              // Set unauthorized requests exception handler
              .exceptionHandling()
              .authenticationEntryPoint(authenticationEntryPoint)
              .and()

              // Set permissions on endpoints
              .authorizeRequests()
              // Public endpoints
              .antMatchers(VIEW_MAPPING).permitAll()
              .antMatchers(STATIC_CONTENT).permitAll()
              // Private endpoints
              .anyRequest()
              .authenticated()
              .and()

              // Set login and logout form
              .formLogin()
              .loginPage("/login").permitAll()
              .and()
              .logout()
              .deleteCookies("JSESSIONID")
              .invalidateHttpSession(true)
              .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
              .logoutSuccessUrl("/?logout").permitAll()
              .and()

              // Add KeyCodeFilter before attempting authentication
              .addFilterBefore(keyCodeFilter, UsernamePasswordAuthenticationFilter.class)

              // Unlock account if lock duration time has expired due to multiple failed login attempts
              .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)

              // Redirect from login/register page if user is authenticated
              .addFilterAfter(redirectPageFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
