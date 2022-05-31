package com.zsoltbalvanyos.ticket.partners.funtickets;

import com.zsoltbalvanyos.ticket.PartnerClient;
import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSummary;
import com.zsoltbalvanyos.ticket.dtos.ResponseWrapper;
import com.zsoltbalvanyos.ticket.mappers.EventDetailsMapper;
import com.zsoltbalvanyos.ticket.mappers.EventSummaryMapper;
import com.zsoltbalvanyos.ticket.partners.besttickets.dtos.BestTicketEvent;
import com.zsoltbalvanyos.ticket.partners.besttickets.dtos.BestTicketSeats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component("FunTickets")
@Slf4j
public class FunTickets implements PartnerClient {


    private final String endpoint;
    private final String apiKey;
    private final EventDetailsMapper eventDetailsMapper;
    private final EventSummaryMapper eventSummaryMapper;
    private final RestTemplate restTemplate;

    private final static String API_KEY_HEADER = "X-API-Key";

    public FunTickets(
        @Value("${partners.fun-tickets.endpoint}") String endpoint,
        @Value("${partners.fun-tickets.api-key}") String apiKey,
        EventDetailsMapper eventDetailsMapper,
        EventSummaryMapper eventSummaryMapper,
        RestTemplate restTemplate
    ) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.eventDetailsMapper = eventDetailsMapper;
        this.eventSummaryMapper = eventSummaryMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<EventSummary> getEvents() {
        var response = restTemplate.exchange(
            RequestEntity
                .get(String.format("%s/getEvents", endpoint))
                .header(API_KEY_HEADER, apiKey)
                .build(),
            new ParameterizedTypeReference<ResponseWrapper<BestTicketEvent[]>>(){}
        );

        if (response.getBody() == null) {
            return List.of();
        }

        return Arrays.stream(response.getBody().data())
            .map(eventSummaryMapper::fromBestTicketEvent)
            .toList();
    }

    @Override
    public Optional<EventDetails> getEvent(long eventId) {
        var response = restTemplate.exchange(
            RequestEntity
                .get(String.format("%s/getEvent?eventId=%d", endpoint, eventId))
                .header(API_KEY_HEADER, apiKey)
                .build(),
            new ParameterizedTypeReference<ResponseWrapper<BestTicketSeats>>(){}
        );

        return Optional.ofNullable(response.getBody())
            .map(ResponseWrapper::data)
            .map(eventDetailsMapper::fromBestTicketSeats);
    }

    @Override
    public Optional<Long> reserveSeat(long eventId, long seatId) {
        var uri = String.format(
            "%s/reserve?eventId=%d&seatId=%d",
            endpoint,
            eventId,
            seatId);

        var response = restTemplate.exchange(
            RequestEntity
                .post(uri)
                .header(API_KEY_HEADER, apiKey)
                .build(),
            new ParameterizedTypeReference<ResponseWrapper<Long>>(){}
        );

        return Optional.ofNullable(response.getBody())
            .map(ResponseWrapper::data);
    }
}
