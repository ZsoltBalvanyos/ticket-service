package com.zsoltbalvanyos.ticket.partners.besttickets;

import com.zsoltbalvanyos.ticket.PartnerClient;
import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSummary;
import com.zsoltbalvanyos.ticket.mappers.EventDetailsMapper;
import com.zsoltbalvanyos.ticket.mappers.EventSummaryMapper;
import com.zsoltbalvanyos.ticket.partners.besttickets.dtos.BestTicketEvent;
import com.zsoltbalvanyos.ticket.partners.besttickets.dtos.BestTicketSeats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component("BestTickets")
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
        var events = restTemplate.getForObject(
            String.format("%s/getEvents", endpoint),
            BestTicketEvent[].class);

        if (events == null) {
            return List.of();
        }

        return Arrays.stream(events)
            .map(eventSummaryMapper::fromBestTicketEvent)
            .toList();
    }

    @Override
    public Optional<EventDetails> getEvent(long eventId) {
        return Optional.ofNullable(
            restTemplate.getForObject(
                String.format("%s/getEvent?eventId=%d", endpoint, eventId),
                BestTicketSeats.class))
            .map(eventDetailsMapper::fromBestTicketSeats);
    }

    @Override
    public long reserveSeat(long eventId, long seatId) {
        return Optional.ofNullable(
            restTemplate.postForObject(
                String.format(
                    "%s/reserve?eventId=%d&seatId=%d",
                    endpoint,
                    eventId,
                    seatId),
                Void.class,
                Long.class))
            .orElse(0L);
    }
}
