package com.travelq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Modified configuration to allow all API endpoints for demo/testing
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll()  // Allow all API endpoints
                .requestMatchers("/h2-console/**").permitAll() // For H2 console access
                // Permit Swagger UI and OpenAPI endpoints
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                // Permit Views
                .requestMatchers("/css/**", "/view/**").permitAll() // For web pages
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        
        // Allow frames for H2 console
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 