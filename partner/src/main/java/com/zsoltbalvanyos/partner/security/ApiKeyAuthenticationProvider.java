package com.zsoltbalvanyos.partner.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Set;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    // replace with some component to validate api key
    private final Set<String> validApiKeys = Set.of("abcd1234", "xyz898989");

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = (String) authentication.getPrincipal();

        if (ObjectUtils.isEmpty(apiKey)) {
            throw new InsufficientAuthenticationException("No API key in request");
        }

        if (validApiKeys.contains(apiKey)) {
            return new ApiKeyAuthenticationToken(apiKey, true);
        }
        throw new BadCredentialsException("API Key is invalid");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }
}