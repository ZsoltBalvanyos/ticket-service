package com.zsoltbalvanyos.ticket.exceptions;

import com.zsoltbalvanyos.ticket.entities.BookingState;
import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {

    long bookingId;
    BookingState bookingState;

    public PaymentException(long bookingId, BookingState bookingState) {
        super();
        this.bookingId = bookingId;
        this.bookingState = bookingState;
    }
}
