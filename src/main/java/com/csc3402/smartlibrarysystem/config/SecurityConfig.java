package com.csc3402.smartlibrarysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // 1. Allow CSS to load
                        .requestMatchers("/css/**").permitAll()
                        // 2. TEMPORARILY allow the dashboard for UI testing
                        .requestMatchers("/dashboard").permitAll()
                        // 2. TEMPORARILY allow the mylibrary for UI testing
                        .requestMatchers("/mylibrary").permitAll()
                        // 2. TEMPORARILY allow the category for UI testing
                        .requestMatchers("/category").permitAll()
                        // 2. TEMPORARILY allow the profile for UI testing
                        .requestMatchers("/profile").permitAll()
                        // 2. TEMPORARILY allow the admin pages for UI testing
                        .requestMatchers("/admin", "/admin/**").permitAll()
                        // 3. Require authentication for everything else
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                );

        return http.build();
    }
}