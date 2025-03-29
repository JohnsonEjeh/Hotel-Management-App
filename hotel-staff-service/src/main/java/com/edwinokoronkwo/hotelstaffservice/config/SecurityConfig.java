//package com.edwinokoronkwo.hotelstaffservice.config;//package com.edwinokoronkwo.hotelstaffservice.config;
////
////import lombok.RequiredArgsConstructor;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
////import org.springframework.security.core.userdetails.UserDetailsService;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
////import org.springframework.security.web.SecurityFilterChain;
////
////import static org.springframework.security.config.Customizer.withDefaults;
////
////
////import com.edwinokoronkwo.hotelstaffservice.service.CustomUserDetailsService;
////
////
////@Configuration
////@EnableWebSecurity
////public class SecurityConfig {
////
////    @Autowired
////    private CustomUserDetailsService userDetailsService;
////
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .authorizeHttpRequests((authz) -> authz
////                        .requestMatchers("/staff/**").hasRole("ADMIN")
////                        .anyRequest().authenticated()
////                )
////                .httpBasic(withDefaults())
////                .userDetailsService(userDetailsService); // Use custom UserDetailsService
////        return http.build();
////    }
////
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////}
//
//
//import org.springframework.boot.autoconfigure.security.SecurityProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                )
//                .formLogin(Customizer.withDefaults())
//                .httpBasic(Customizer.withDefaults());
//        return http.build();
//    }
//}

//@EnableWebSecurity
//public class SecurityConfig{
//
//    @Bean
//
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .anyRequest()
//                .authenticated()
//                        .and()
//                                .httpBasic();
//
//        http.formLogin();
//        http.httpBasic();
//        return http.build();
//    }
//
//}