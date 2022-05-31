package com.zsoltbalvanyos.ticket.partners.besttickets;

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
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component("BestTickets")
@Slf4j
public class BestTickets implements PartnerClient {

    private final String endpoint;
    private final EventDetailsMapper eventDetailsMapper;
    private final EventSummaryMapper eventSummaryMapper;
    private final RestTemplate restTemplate;

    public BestTickets(
        @Value("${partners.best-tickets.endpoint}") String endpoint,
        EventDetailsMapper eventDetailsMapper,
        EventSummaryMapper eventSummaryMapper,
        RestTemplate restTemplate
    ) {
        this.endpoint = endpoint;
        this.eventDetailsMapper = eventDetailsMapper;
        this.eventSummaryMapper = eventSummaryMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<EventSummary> getEvents() {
        var response = restTemplate.exchange(
            String.format("%s/getEvents", endpoint),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ResponseWrapper<BestTicketEvent[]>>(){});

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
            String.format("%s/getEvent?eventId=%d", endpoint, eventId),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<ResponseWrapper<BestTicketSeats>>(){});

        return Optional.ofNullable(response.getBody())
            .map(ResponseWrapper::data)
            .map(eventDetailsMapper::fromBestTicketSeats);
    }

    @Override
    public Optional<Long> reserveSeat(long eventId, long seatId) {
        var response = restTemplate.exchange(
            String.format(
                "%s/reserve?eventId=%d&seatId=%d",
                endpoint,
                eventId,
                seatId),
            HttpMethod.POST,
            null,
            new ParameterizedTypeReference<ResponseWrapper<Long>>(){});

        return Optional.ofNullable(response.getBody())
            .map(ResponseWrapper::data);
    }
}
