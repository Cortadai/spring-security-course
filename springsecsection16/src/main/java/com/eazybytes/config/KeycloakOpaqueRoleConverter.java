package com.eazybytes.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenAuthenticationConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeycloakOpaqueRoleConverter implements OpaqueTokenAuthenticationConverter {

    @Override
    public Authentication convert(String introspectedToken, OAuth2AuthenticatedPrincipal authenticatedPrincipal) {
        String username = authenticatedPrincipal.getAttribute("preferred_username");
        Map<String, Object> realmAccess = authenticatedPrincipal.getAttribute("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(String roleName : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        }
        return new UsernamePasswordAuthenticationToken(authenticatedPrincipal.getName(), null, grantedAuthorities);
    }
}
