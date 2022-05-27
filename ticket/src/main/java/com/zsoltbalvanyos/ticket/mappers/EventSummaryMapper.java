package com.zsoltbalvanyos.ticket.mappers;

import com.zsoltbalvanyos.ticket.dtos.EventSummary;
import com.zsoltbalvanyos.ticket.partners.besttickets.dtos.BestTicketEvent;
import org.springframework.stereotype.Component;

@Component
public class EventSummaryMapper {

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
