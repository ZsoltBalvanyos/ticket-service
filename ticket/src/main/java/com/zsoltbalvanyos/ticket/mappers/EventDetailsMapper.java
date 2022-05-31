package com.zsoltbalvanyos.ticket.mappers;

import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSeat;
import com.zsoltbalvanyos.ticket.partners.besttickets.dtos.BestTicketSeats;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

// TODO: 27/05/2022 find a mapper library that works with record type

/**
 * Converts response objects from different partners into and internal representation of an event
 */
@Component
public class EventDetailsMapper {

    /**
     * Converts the event details response received from the BestTickets partner
     * to the internal representation
     *
     * @param seats response body from BestTickets listing seats of an event
     * @return details of an event.
     */
    public EventDetails fromBestTicketSeats(BestTicketSeats seats) {
        return new EventDetails(
            seats.eventId(),
            seats.seats()
                .stream()
                .map(s ->
                    new EventSeat(
                        s.seatId(),
                        s.price(),
                        s.currency(),
                        s.reserved()))
                .collect(Collectors.toList()));
    }
}
