package com.zsoltbalvanyos.ticket.services;

import com.zsoltbalvanyos.ticket.PartnerClient;
import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSummary;
import com.zsoltbalvanyos.ticket.exceptions.ErrorCode;
import com.zsoltbalvanyos.ticket.exceptions.TicketException;
import com.zsoltbalvanyos.ticket.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
public class TicketService {

    private final PaymentService paymentService;
    private final PartnerRepository partnerRepository;
    private final Map<String, PartnerClient> partnerClients;

    // TODO: 27/05/2022 these could be parallel
    public List<EventSummary> getEvents() {
        return partnerRepository.getPartnersWithActiveEvent()
            .stream()
            .flatMap(partner ->
                Optional.ofNullable(partnerClients.get(partner.getName()))
                    .map(PartnerClient::getEvents)
                    .map(List::stream)
                    .orElseGet(() -> {
                        log.error("partner {} not found", partner.getName());
                        return Stream.of();}))
            .collect(Collectors.toList());
    }

    public Optional<EventDetails> getEvent(long eventId) {
        return Optional.ofNullable(
                partnerClients.get(
                    partnerRepository
                        .getPartnerOfEvent(eventId)
                        .orElseThrow(() -> new TicketException(ErrorCode.EVENT_NOT_FOUND))
                        .getName()))
            .orElseThrow(() -> new TicketException(ErrorCode.PARTNER_NOT_FOUND))
            .getEvent(eventId);
    }

    public long buyTicket(long eventId, long seatId, long userId, long cardId) {

        var partner = partnerRepository
            .getPartnerOfEvent(eventId)
            .orElseThrow(() -> new TicketException(ErrorCode.EVENT_NOT_FOUND));

        var allEventsOfPartner = Optional.ofNullable(partnerClients.get(partner.getName()))
            .map(PartnerClient::getEvents)
            .orElseThrow(() -> new TicketException(ErrorCode.PARTNER_NOT_FOUND));

        var eventStartTimestamp = Optional.ofNullable(
            allEventsOfPartner
                .stream()
                .collect(Collectors.toMap(EventSummary::eventId, EventSummary::startTimestamp))
                .get(eventId))
            .orElseThrow(() -> new TicketException(ErrorCode.EVENT_NOT_FOUND));

        if (Instant.ofEpochMilli(eventStartTimestamp).isBefore(Instant.now())) {
            throw new TicketException(ErrorCode.EVENT_IN_PAST);
        }

        var eventDetails = getEvent(eventId)
            .orElseThrow(() -> new TicketException(ErrorCode.EVENT_NOT_FOUND));

        var seat = eventDetails.seats().stream()
            .filter(s -> s.seatId() == seatId)
            .findFirst()
            .orElseThrow(() -> new TicketException(ErrorCode.SEAT_NOT_FOUND));

        if (seat.reserved()) {
            throw new TicketException(ErrorCode.SEAT_RESERVED);
        }

        var partnerClient = Optional
            .ofNullable(partnerClients.get(partner.getName()))
            .orElseThrow(() -> new TicketException(ErrorCode.PARTNER_NOT_FOUND));

        return paymentService.buyTicket(
            partnerClient,
            eventDetails,
            seat,
            userId,
            partner.getPartnerId(),
            cardId);
    }
}
