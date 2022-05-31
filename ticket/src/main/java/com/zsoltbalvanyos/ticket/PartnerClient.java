package com.zsoltbalvanyos.ticket;

import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSummary;

import java.util.List;
import java.util.Optional;

public interface PartnerClient {

    /**
     * Returns all events
     *
     * @return all events
     */
    List<EventSummary> getEvents();

    /**
     * Returns details of a specific event.
     *
     * @param eventId the id of the event.
     * @return the details of the event
     */
    Optional<EventDetails> getEvent(long eventId);

    /**
     * Reserves a seat of the specified event.
     *
     * @param eventId the id of the event
     * @param seatId the id of the seat
     * @return the reservation id
     */
    Optional<Long> reserveSeat(long eventId, long seatId);

}
