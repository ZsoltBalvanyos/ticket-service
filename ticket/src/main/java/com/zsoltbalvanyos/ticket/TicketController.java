package com.zsoltbalvanyos.ticket;

import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSummary;
import com.zsoltbalvanyos.ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/getEvents")
    public List<EventSummary> getEvents() {
        return ticketService.getEvents();
    }

    @GetMapping("/getEvent")
    public EventDetails getEvents(@RequestParam("eventId") long eventId) {
        return ticketService.getEvent(eventId).get();
    }

    @PostMapping("/pay")
    public long pay(
        @RequestParam("eventId") long eventId,
        @RequestParam("seatId") long seatId,
        @RequestParam("cardId") long cardId
    ) {
        return ticketService.buyTicket(eventId, seatId, 1000, cardId);
    }

}
