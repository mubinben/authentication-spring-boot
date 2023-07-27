package com.example.authentication.config;

import com.example.authentication.exception.EntryPointException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
        String[] authorizedPath = {
                "/api/account/register",
                "/api/account/login",
                "/api/account/logout"
        };
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handle -> handle.authenticationEntryPoint(new EntryPointException()))
                .authorizeHttpRequests(request -> request.requestMatchers(HttpMethod.POST, authorizedPath).permitAll())
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .build();
    }

}