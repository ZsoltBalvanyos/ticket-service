package com.zsoltbalvanyos.ticket.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Optional;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    private long bookingId;
    private long userId;
    private long cardId;
    private long eventId;
    private long seatId;
    private Long reservationId;

    public Optional<Long> getReservationId() {
        return Optional.ofNullable(reservationId);
    }
}
