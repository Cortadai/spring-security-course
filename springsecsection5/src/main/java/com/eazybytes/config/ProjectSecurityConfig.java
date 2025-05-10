package com.eazybytes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());
//        http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());
        http.authorizeHttpRequests((requests) -> requests
                // Rutas seguras
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                // Rutas públicas
                .requestMatchers("/notices", "/contact", "/error", "/register").permitAll()
        );
//        http.formLogin(flc -> flc.disable()); // Desactivar formLogin
//        http.httpBasic(hbc -> hbc.disable()); // Desactivar httpBasic
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }

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
