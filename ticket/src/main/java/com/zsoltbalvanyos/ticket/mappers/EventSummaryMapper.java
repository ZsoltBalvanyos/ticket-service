package com.zsoltbalvanyos.ticket.mappers;

import com.zsoltbalvanyos.ticket.dtos.EventSummary;
import com.zsoltbalvanyos.ticket.partners.besttickets.dtos.BestTicketEvent;
import org.springframework.stereotype.Component;

/**
 * Converts response objects from different partners into and internal representation of an event
 */
@Component
public class EventSummaryMapper {

    /**
     * Converts the event summary received from the BestTickets partner
     * to the internal representation
     *
     * @param bestTicketEvent response body from BestTickets of an event
     * @return summary of an event
     */
    public EventSummary fromBestTicketEvent(BestTicketEvent bestTicketEvent) {
        return new EventSummary(
            bestTicketEvent.eventId(),
            bestTicketEvent.title(),
            bestTicketEvent.location(),
            bestTicketEvent.startTimestamp(),
            bestTicketEvent.endTimestamp()
        );
    }
}
