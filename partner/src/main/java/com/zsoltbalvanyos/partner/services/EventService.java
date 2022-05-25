package com.zsoltbalvanyos.partner.services;

import com.zsoltbalvanyos.partner.dtos.EventSeat;
import com.zsoltbalvanyos.partner.dtos.EventSummary;
import com.zsoltbalvanyos.partner.dtos.EventDetails;
import com.zsoltbalvanyos.partner.exceptions.EventUnavailableException;
import com.zsoltbalvanyos.partner.exceptions.SeatReservedException;
import com.zsoltbalvanyos.partner.exceptions.SeatUnavailableException;
import com.zsoltbalvanyos.partner.repositories.EventRepository;
import com.zsoltbalvanyos.partner.repositories.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public long reserve(long eventId, long seatId) {
        var reservation = seatRepository
            .findByEventIdAndSeatId(eventId, seatId)
            .orElseThrow(SeatUnavailableException::new);

        if (reservation.isReserved()) {
            throw new SeatReservedException();
        }

        reservation.setReserved(true);
        return reservation.getReservationId();
    }

    public List<EventSummary> getEvents() {
        return eventRepository.findAll().stream().map(event -> new EventSummary(
            event.getEventId(),
            event.getTitle(),
            event.getLocation(),
            event.getStartTimestamp().toEpochMilli(),
            event.getEndTimestamp().toEpochMilli()
        )).collect(Collectors.toList());
    }

    public EventDetails getEvent(@RequestParam("eventId") long eventId) {
        var seats = seatRepository
            .findByEventId(eventId)
            .stream()
            .map(s -> new EventSeat(s.getSeatId(), s.getPrice(), s.getCurrency(), s.isReserved()))
            .toList();

        if (seats.isEmpty()) throw new EventUnavailableException();

        return new EventDetails(eventId, seats);
    }
}
