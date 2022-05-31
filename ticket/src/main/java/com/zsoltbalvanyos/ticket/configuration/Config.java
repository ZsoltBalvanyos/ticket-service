package com.zsoltbalvanyos.ticket.configuration;

import com.zsoltbalvanyos.ticket.utils.LoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Config {
//    ResponseErrorHandler responseErrorHandler
    @Bean
    public RestTemplate restTemplate(LoggingInterceptor loggingInterceptor) {
        var restTemplate = new RestTemplate();
//        restTemplate.setInterceptors(List.of(loggingInterceptor));
//        restTemplate.setErrorHandler(responseErrorHandler);
        return restTemplate;
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
            .build();
    }
}
