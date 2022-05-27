package com.zsoltbalvanyos.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
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

    @GetMapping(value = "**", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object get(HttpServletRequest request, ProxyExchange<Object> proxy) {
        var uri = getTicketUrl(request);
        log.debug("forwarding GET request to {}", uri);
        return proxy.uri(uri).get().getBody();
    }

    @PostMapping(value = "**", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object post(HttpServletRequest request, ProxyExchange<Object> proxy) {
        var uri = getTicketUrl(request);
        log.debug("forwarding POST request to {}", uri);
        return proxy.uri(uri).post().getBody();
    }

    private String getTicketUrl(HttpServletRequest request) {
        var queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        var requestUri = request.getRequestURI() == null ? "" : request.getRequestURI();
        return ticketEndpoint + requestUri + queryString;
    }
}
