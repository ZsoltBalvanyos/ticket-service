package com.zsoltbalvanyos.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
public class ApiController {

    @Value("ticket.endpoint")
    private final String ticketEndpoint = "http://localhost:9993";  // TODO: 24/05/2022 move to config

    @GetMapping("**")
    public String get(HttpServletRequest request, ProxyExchange<String> proxy) {
        return proxy.uri(getTicketUrl(request)).get().getBody();
    }

    @PostMapping("**")
    public String post(HttpServletRequest request, ProxyExchange<String> proxy) {
        return proxy.uri(getTicketUrl(request)).post().getBody();
    }

    private String getTicketUrl(HttpServletRequest request) {
        var queryString = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        return ticketEndpoint + request.getPathInfo() + queryString;
    }
}
