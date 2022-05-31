package com.zsoltbalvanyos.ticket.services;

import com.zsoltbalvanyos.ticket.CoreClient;
import com.zsoltbalvanyos.ticket.PartnerClient;
import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.dtos.EventSeat;
import com.zsoltbalvanyos.ticket.entities.Booking;
import com.zsoltbalvanyos.ticket.entities.BookingStatus;
import com.zsoltbalvanyos.ticket.exceptions.PaymentException;
import com.zsoltbalvanyos.ticket.repositories.BookingRepository;
import com.zsoltbalvanyos.ticket.repositories.BookingStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.zsoltbalvanyos.ticket.entities.BookingState.AMOUNT_RESERVED;
import static com.zsoltbalvanyos.ticket.entities.BookingState.PAYMENT_COMPLETED;
import static com.zsoltbalvanyos.ticket.entities.BookingState.PAYMENT_REVERTED;
import static com.zsoltbalvanyos.ticket.entities.BookingState.REVERT_PAYMENT_FAILED;
import static com.zsoltbalvanyos.ticket.entities.BookingState.SEAT_BOOKED;
import static com.zsoltbalvanyos.ticket.entities.BookingState.SEAT_BOOKING_FAILED;
import static com.zsoltbalvanyos.ticket.entities.BookingState.STARTED;


@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final CoreClient coreClient;
    private final BookingRepository bookingRepository;
    private final BookingStatusRepository bookingStatusRepository;

    public long buyTicket(PartnerClient partnerClient, EventDetails event, EventSeat seat, long userId, long partnerId, long cardId) {
        var booking = bookingRepository.save(
            Booking.builder()
                .userId(userId)
                .cardId(cardId)
                .seatId(seat.seatId())
                .eventId(event.eventId())
                .build());

        var statusBuilder = BookingStatus.builder().bookingId(booking.getBookingId());
        bookingStatusRepository.save(statusBuilder.status(STARTED).build());

        try {
            coreClient.reserveAmount(booking.getBookingId(), userId, cardId, seat.price());
            bookingStatusRepository.save(statusBuilder.status(AMOUNT_RESERVED).build());

            var reservationId = partnerClient.reserveSeat(event.eventId(), seat.seatId())
                .orElseThrow(() -> {
                    revertReservation(booking.getBookingId(), userId, cardId, statusBuilder);
                    return new PaymentException(booking.getBookingId(), SEAT_BOOKING_FAILED);
                });
            bookingStatusRepository.save(statusBuilder.status(SEAT_BOOKED).build());

            coreClient.completePayment(booking.getBookingId(), partnerId);
            bookingStatusRepository.save(statusBuilder.status(PAYMENT_COMPLETED).build());

            log.info("booking {} completed successfully", booking.getBookingId());

            return reservationId;
        } catch (PaymentException pe) {
            bookingStatusRepository.save(statusBuilder.status(pe.getBookingState()).build());
            log.warn("booking {} failed: {}", booking.getBookingId(), pe.getBookingState());
            throw pe;
        }
    }

    private void revertReservation(long bookingId, long userId, long cardId, BookingStatus.BookingStatusBuilder statusBuilder) {
        try {
            coreClient.revertTransaction(bookingId, userId, cardId);
            bookingStatusRepository.save(statusBuilder.status(PAYMENT_REVERTED).build());
        } catch (PaymentException revertEx) {
            bookingStatusRepository.save(statusBuilder.status(REVERT_PAYMENT_FAILED).build());
            log.error("failed to revert transaction {}", bookingId);
            throw revertEx;
        }
    }
}
