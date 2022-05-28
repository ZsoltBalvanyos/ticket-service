package com.zsoltbalvanyos.api;

import com.zsoltbalvanyos.api.exceptions.UserTokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@Slf4j
public class ApiController {

    private final String coreEndpoint;
    private final String ticketEndpoint;
    private final RestTemplate restTemplate;

    public ApiController(
        @Value("${core.endpoint}") String coreEndpoint,
        @Value("${ticket.endpoint}") String ticketEndpoint,
        RestTemplate restTemplate
    ) {
        this.coreEndpoint = coreEndpoint;
        this.ticketEndpoint = ticketEndpoint;
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "**", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object get(HttpServletRequest request, ProxyExchange<Object> proxy) {
        validateUser(request);
        var uri = getTicketUrl(request);
        log.debug("forwarding GET request to {}", uri);
        return proxy.uri(uri).get().getBody();
    }

    @PostMapping(value = "**", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object post(HttpServletRequest request, ProxyExchange<Object> proxy) {
        validateUser(request);
        var uri = getTicketUrl(request);
        log.debug("forwarding POST request to {}", uri);
        return proxy.uri(uri).post().getBody();
    }

    private String getTicketUrl(HttpServletRequest request) {
        var queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        var requestUri = request.getRequestURI() == null ? "" : request.getRequestURI();
        return ticketEndpoint + requestUri + queryString;
    }

    private void validateUser(HttpServletRequest request) {
        var path = coreEndpoint + "/user/validate?token=";
        var url = Optional
            .ofNullable(request.getHeader("User-Token"))
            .map(path::concat)
            .orElseThrow(UserTokenNotFoundException::new);

        restTemplate.getForObject(url, Void.class);
    }
}
