package com.zsoltbalvanyos.ticket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {

    @GetMapping("/getEvents")
    public String getEvents() {
        return "get all events";
    }

    @GetMapping("/getEvent")
    public String getEvents(@RequestParam("eventId") long eventId) {
        return "get event " + eventId;
    }

    @PostMapping("/pay")
    public String pay(
        @RequestParam("eventId") long eventId,
        @RequestParam("seatId") long seatId
    ) {
        return String.format("seat %d for event %d has been reserved", seatId, eventId);
    }

}
