package com.cloud.secure.streaming.config.security;

import com.cloud.secure.streaming.controllers.ApiPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @author 689Cloud
 */
@Configuration
public class SecurityConfiguration {

    @Autowired
    private AuthEntryPointException unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationTokenFilterBean() {
        return new AuthTokenFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // Allow access public resource
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.png",
                        "/**/*.gif",
                        "/public/**",
                        "/**/public",
                        "/**/public/**",
                        "/**/*.json",
                        "/**/*.jpg",
                        // enable swagger endpoints
                        "/swagger-resources/**",
                        "/swagger-ui.html**",
                        "/api/**",
                        "/webjars/**",
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/configuration/security",
                        "/manage/api-docs",
                        "/v3/api-docs/**"
                ).permitAll()
                // allow CORS option calls
                .antMatchers(HttpMethod.OPTIONS, ApiPath.BASE_API_PATH + "/**").permitAll()
                .antMatchers(ApiPath.BASE_API_PATH + "/**/public/**").permitAll()
                .antMatchers(HttpMethod.POST, ApiPath.USER_API + "/**").permitAll()
                .antMatchers(HttpMethod.POST, ApiPath.AUTHENTICATE_API + "/**").permitAll()
                .antMatchers(HttpMethod.POST, ApiPath.AUTHENTICATE_API + "/password" + "/**").permitAll()
                .antMatchers(HttpMethod.PUT, ApiPath.AUTHENTICATE_API + "/password/{code}" + "/**").permitAll()
                .antMatchers(HttpMethod.PUT, ApiPath.AUTHENTICATE_API + "/reset-password/{code}" + "/**").permitAll()
                .antMatchers(HttpMethod.GET, ApiPath.CATEGORY_API + "/**").permitAll()
                // All other request must be specified token
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
        httpSecurity.headers().frameOptions().disable();


        return httpSecurity.build();
    }
}