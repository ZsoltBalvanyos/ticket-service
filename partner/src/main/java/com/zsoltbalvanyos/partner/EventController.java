package com.zsoltbalvanyos.partner;

import com.zsoltbalvanyos.partner.dtos.EventSummary;
import com.zsoltbalvanyos.partner.dtos.EventDetails;
import com.zsoltbalvanyos.partner.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/getEvents")
    public List<EventSummary> getEvents() {
        return eventService.getEvents();
    }

    @GetMapping("/getEvent")
    public EventDetails getEvent(@RequestParam("eventId") long eventId) {
        return eventService.getEvent(eventId);
    }

    @PostMapping("/reserve")
    public Long reserve(
        @RequestParam("eventId") long eventId,
        @RequestParam("seatId") long seatId
    ) {
        return eventService.reserve(eventId, seatId);
    }

}
