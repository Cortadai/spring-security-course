package com.eazybytes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        http.authorizeHttpRequests((requests) -> requests
                // Rutas seguras
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                // Rutas públicas
                .requestMatchers("/notices", "/contact", "/error").permitAll()
        );
//        http.formLogin(flc -> flc.disable()); // Desactivar formLogin
//        http.httpBasic(hbc -> hbc.disable()); // Desactivar httpBasic
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
//        https://bcrypt-generator.com/
        UserDetails user = User
                .withUsername("user")
                .password("{noop}@*AJZVVj5#dwB0")
                .authorities("read").
                build();
        UserDetails admin = User
                .withUsername("admin")
                .password("{bcrypt}$2a$12$PcB3MwQJn6ZG0L4nLAMFMebNqmAGlhmWrxneKqK3jh6Z93beUjehS")   // D%0c@nc$4wO&4w
                .authorities("admin")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    From Spring Security 6.3 onwards
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

}
