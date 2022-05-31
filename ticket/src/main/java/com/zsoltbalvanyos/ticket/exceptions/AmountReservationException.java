package com.zsoltbalvanyos.ticket.exceptions;

import com.zsoltbalvanyos.ticket.entities.BookingState;

public class AmountReservationException extends PaymentException {
    public AmountReservationException(long bookingId, BookingState bookingState) {
        super(bookingId, bookingState);
    }
}
