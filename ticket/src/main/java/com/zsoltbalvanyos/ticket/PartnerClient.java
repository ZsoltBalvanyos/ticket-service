package com.zsoltbalvanyos.ticket;

import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSummary;

import java.util.List;
import java.util.Optional;

public interface PartnerClient {

    List<EventSummary> getEvents();

    Optional<EventDetails> getEvent(long eventId);

    long reserveSeat(long eventId, long seatId);

}
