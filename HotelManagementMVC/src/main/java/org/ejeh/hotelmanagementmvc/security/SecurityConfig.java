package org.ejeh.hotelmanagementmvc.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for API testing
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()  // Allow all requests
                )
                .formLogin(form -> form.disable())  // Disable default form login
                .httpBasic(basic -> basic.disable());  // Disable basic auth

        return http.build();
    }
}
