package com.zsoltbalvanyos.ticket;

import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSummary;
import com.zsoltbalvanyos.ticket.dtos.ResponseWrapper;
import com.zsoltbalvanyos.ticket.exceptions.ErrorCode;
import com.zsoltbalvanyos.ticket.exceptions.TicketException;
import com.zsoltbalvanyos.ticket.services.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/getEvents")
    public ResponseWrapper<List<EventSummary>> getEvents() {
        return new ResponseWrapper<>(ticketService.getEvents(), true);
    }

    @GetMapping("/getEvent")
    public ResponseWrapper<EventDetails> getEvents(@RequestParam("eventId") long eventId) {
        var eventDetails = ticketService.getEvent(eventId)
            .orElseThrow(() -> new TicketException(ErrorCode.EVENT_NOT_FOUND));
        return new ResponseWrapper<>(eventDetails, true);
    }

    @PostMapping("/pay")
    public ResponseWrapper<Long> pay(
        @RequestParam("eventId") long eventId,
        @RequestParam("seatId") long seatId,
        @RequestParam("cardId") long cardId,
        @RequestHeader("User-Token") String token
    ) {
        var parts = new String(Base64.getDecoder().decode(token)).split("&");
        return new ResponseWrapper<>(ticketService.buyTicket(eventId, seatId, Long.parseLong(parts[1]), cardId), true);
    }

}
