package com.zsoltbalvanyos.ticket.mappers;

import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSeat;
import com.zsoltbalvanyos.ticket.partners.besttickets.dtos.BestTicketSeats;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

// TODO: 27/05/2022 find a mapper library that works with record type
@Component
public class EventDetailsMapper {

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
