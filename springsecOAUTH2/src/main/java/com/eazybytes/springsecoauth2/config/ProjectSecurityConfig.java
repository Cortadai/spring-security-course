package com.eazybytes.springsecoauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/secure").authenticated()
                .anyRequest().permitAll()
        );
        http.formLogin(Customizer.withDefaults());
        http.oauth2Login(Customizer.withDefaults());
        return http.build();
    }

    /*
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration github = githubClientRegistration();
        ClientRegistration facebook = facebookClientRegistration();
        return new InMemoryClientRegistrationRepository(github, facebook);
    }

    private ClientRegistration githubClientRegistration() {
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
                .clientId("asd").clientSecret("asd").build();
    }

    private ClientRegistration facebookClientRegistration() {
        return CommonOAuth2Provider.FACEBOOK.getBuilder("facebook")
                .clientId("asd").clientSecret("asd").build();
    }
    */
}
