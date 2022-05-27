package com.zsoltbalvanyos.ticket.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(BookingStatus.BookingStatusId.class)
public class BookingStatus implements Serializable {

    @Id
    private long bookingId;

    @Id
    @Enumerated(EnumType.STRING)
    private BookingState status;

    public static class BookingStatusId implements Serializable {
        private long bookingId;
        private BookingState status;
    }
}
