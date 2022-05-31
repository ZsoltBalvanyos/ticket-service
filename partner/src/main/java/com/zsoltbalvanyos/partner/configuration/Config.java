package com.zsoltbalvanyos.partner.configuration;

import com.zsoltbalvanyos.partner.security.ApiKeyAuthenticationFilter;
import com.zsoltbalvanyos.partner.security.ApiKeyAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

@Configuration
@EnableWebSecurity
public class Config {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        return http
            .authorizeHttpRequests((authz) -> authz
                .anyRequest().authenticated().and().addFilterBefore(
                    new ApiKeyAuthenticationFilter(authenticationManager),
                    AnonymousAuthenticationFilter.class
                )
            )
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        ApiKeyAuthenticationProvider apiKeyAuthenticationProvider
    ) {
        return new ProviderManager(List.of(apiKeyAuthenticationProvider));
    }
}
