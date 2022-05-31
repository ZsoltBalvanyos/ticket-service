package com.zsoltbalvanyos.partner;

import com.zsoltbalvanyos.partner.dtos.EventDetails;
import com.zsoltbalvanyos.partner.dtos.EventSummary;
import com.zsoltbalvanyos.partner.dtos.ResponseWrapper;
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
    public ResponseWrapper<List<EventSummary>> getEvents() {
        return new ResponseWrapper<>(eventService.getEvents(), true);
    }

    @GetMapping("/getEvent")
    public ResponseWrapper<EventDetails> getEvent(@RequestParam("eventId") long eventId) {
//        throw new RuntimeException("booom");
        return new ResponseWrapper<>(eventService.getEvent(eventId), true);
    }

    @PostMapping("/reserve")
    public ResponseWrapper<Long> reserve(
        @RequestParam("eventId") long eventId,
        @RequestParam("seatId") long seatId
    ) {
        return new ResponseWrapper<>(eventService.reserve(eventId, seatId), true);
    }

}
