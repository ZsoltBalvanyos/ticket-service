package com.zsoltbalvanyos.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class ApiController {

    private final String coreEndpoint;
    private final String ticketEndpoint;

    public ApiController(
        @Value("${core.endpoint}") String coreEndpoint,
        @Value("${ticket.endpoint}") String ticketEndpoint
    ) {
        this.coreEndpoint = coreEndpoint;
        this.ticketEndpoint = ticketEndpoint;
    }

    @GetMapping(path = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> get(HttpServletRequest request, ProxyExchange<?> proxy) {
        var validationResult = validateUser(request, proxy);
        if (validationResult.getStatusCode().isError()) {
            log.error("user token validation failed: {}", validationResult);
            return validationResult;
        }
        return proxy.uri(buildUri(request)).get();
    }

    @PostMapping(value = "/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> post(HttpServletRequest request, ProxyExchange<?> proxy) {
        var validationResult = validateUser(request, proxy);
        if (validationResult.getStatusCode().isError()) {
            log.error("user token validation failed: {}", validationResult);
            return validationResult;
        }
        return proxy.uri(buildUri(request)).post();
    }

    private String getQueryString(HttpServletRequest request) {
        return request.getQueryString() == null ? "" : "?" + request.getQueryString();
    }

    private String getRequestUri(HttpServletRequest request) {
        return request.getRequestURI() == null ? "" : request.getRequestURI();
    }

    private String buildUri(HttpServletRequest request) {
        var uri = ticketEndpoint + getRequestUri(request) + getQueryString(request);
        log.debug("sending {} request to {}", request.getMethod(), uri);
        return uri;
    }

    private ResponseEntity<?> validateUser(HttpServletRequest request, ProxyExchange<?> proxy) {
        return proxy.uri(coreEndpoint + "/user/validate" + getQueryString(request)).get();
    }
}
