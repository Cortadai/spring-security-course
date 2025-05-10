package com.eazybytes.config;

import com.eazybytes.exceptionhandling.CustomAccessDeniedHandler;
import com.eazybytes.filter.CsrfCookieFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static com.eazybytes.constants.ApplicationConstants.JWT_HEADER;

@Configuration
@Profile("!prod")
public class ProjectSecurityConfig {

//    @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}")
//    String introspectionUri;
//
//    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
//    String clientId;
//
//    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
//    String clientSecret;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
        http.cors(corsConfig -> corsConfig.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setExposedHeaders(Arrays.asList(JWT_HEADER));
                config.setMaxAge(3600L);
                return config;
            }
        }));
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrfConfig -> csrfConfig
                .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                .ignoringRequestMatchers("/contact", "/register")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
        http.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class);
        http.requiresChannel(rcc -> rcc.anyRequest().requiresInsecure()); // Only HTTP
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/myAccount").hasRole("USER") // we don't use prefix ROLE_USER, only USER
                .requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/myLoans").authenticated()
                .requestMatchers("/myCards").hasRole("USER")
                .requestMatchers("/user").authenticated()
                .requestMatchers("/notices", "/contact", "/error", "/register").permitAll()
        );
        http.oauth2ResourceServer(rsc -> rsc.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));
//        http.oauth2ResourceServer(rsc -> rsc.opaqueToken(otc -> otc.authenticationConverter(new KeycloakOpaqueRoleConverter())
//                .introspectionUri(this.introspectionUri).introspectionClientCredentials(this.clientId, this.clientSecret)));
        http.exceptionHandling(ehc-> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        return http.build();
    }

}
