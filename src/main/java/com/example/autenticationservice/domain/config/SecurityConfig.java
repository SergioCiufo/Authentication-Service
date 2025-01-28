/* A SCOPO DI ESEMPIO
package com.example.autenticationservice.domain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/login", "/register", "/verify-otp", "/otp/reSend", "/verify-token", "/otp/reSend", "/token/refresh").permitAll() // Permette queste rotte senza autenticazione
                        .anyRequest().authenticated() // Permette tutte le richieste senza autenticazione
                )
                .csrf(csrf -> csrf
                        .disable() // Disabilita CSRF per le API
                );

        return httpSecurity.build();
    }
}
*/