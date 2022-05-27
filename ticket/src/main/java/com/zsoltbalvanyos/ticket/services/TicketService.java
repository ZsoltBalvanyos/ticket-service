package com.zsoltbalvanyos.ticket.services;

import com.zsoltbalvanyos.ticket.PartnerClient;
import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSummary;
import com.zsoltbalvanyos.ticket.exceptions.EventNotFoundException;
import com.zsoltbalvanyos.ticket.exceptions.PartnerNotFoundException;
import com.zsoltbalvanyos.ticket.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
                        .orElseThrow(EventNotFoundException::new)
                        .getName()))
            .orElseThrow(PartnerNotFoundException::new)
            .getEvent(eventId);
    }

    public long buyTicket(long eventId, long seatId, long userId, long cardId) {

        var partner = partnerRepository
            .getPartnerOfEvent(eventId)
            .orElseThrow(EventNotFoundException::new);

        var event = getEvent(eventId).orElseThrow(EventNotFoundException::new);
        var partnerClient = Optional.ofNullable(partnerClients.get(partner.getName())).orElseThrow(PartnerNotFoundException::new);

        return paymentService.buyTicket(
            partnerClient,
            event,
            seatId,
            userId,
            partner.getPartnerId(),
            cardId);
    }
}
