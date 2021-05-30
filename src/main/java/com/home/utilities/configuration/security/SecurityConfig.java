package com.home.utilities.configuration.security;

import com.home.utilities.configuration.security.filters.RedirectLoginPageFilter;
import com.home.utilities.configuration.userdetails.service.CustomUserDetailsService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
      securedEnabled = true,
      jsr250Enabled = true,
      prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] VIEW_MAPPING = {"/", "/#", "/register", "/login/**"};
    private static final String[] STATIC_CONTENT = {"/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg",
          "/**/*.jpg", "/**/*.html", "/**/*.css", "/**/*.js"};

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth
              .userDetailsService(customUserDetailsService)
              .passwordEncoder(passwordEncoder());
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
              .authenticationEntryPoint(customAuthenticationEntryPoint)
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

              // Redirect from login page if user is authenticated
              .addFilterAfter(new RedirectLoginPageFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
