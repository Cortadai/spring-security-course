package com.eazybytes.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        ArrayList<String> roles = (ArrayList<String>) source.getClaims().get("roles");
        if (roles == null || roles.isEmpty()) {
            return new ArrayList<>();
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for(String roleName : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        }
        return grantedAuthorities;
    }

}
