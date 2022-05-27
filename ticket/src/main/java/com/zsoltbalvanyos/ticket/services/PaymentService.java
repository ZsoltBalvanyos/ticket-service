package com.zsoltbalvanyos.ticket.services;

import com.zsoltbalvanyos.ticket.CoreClient;
import com.zsoltbalvanyos.ticket.PartnerClient;
import com.zsoltbalvanyos.ticket.dtos.EventDetails;
import com.zsoltbalvanyos.ticket.entities.Booking;
import com.zsoltbalvanyos.ticket.entities.BookingState;
import com.zsoltbalvanyos.ticket.entities.BookingStatus;
import com.zsoltbalvanyos.ticket.exceptions.PaymentException;
import com.zsoltbalvanyos.ticket.exceptions.SeatNotFoundException;
import com.zsoltbalvanyos.ticket.repositories.BookingRepository;
import com.zsoltbalvanyos.ticket.repositories.BookingStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.zsoltbalvanyos.ticket.entities.BookingState.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final CoreClient coreClient;
    private final BookingRepository bookingRepository;
    private final BookingStatusRepository bookingStatusRepository;

    public long buyTicket(PartnerClient partnerClient, EventDetails event, long seatId, long userId, long partnerId, long cardId) {
        var booking = bookingRepository.save(
            Booking.builder()
                .userId(userId)
                .cardId(cardId)
                .seatId(seatId)
                .eventId(event.eventId())
                .build());

        var seat = event.seats().stream().filter(s -> s.seatId() == seatId).findFirst().orElseThrow(SeatNotFoundException::new);
        var statusBuilder = BookingStatus.builder().bookingId(booking.getBookingId());
        bookingStatusRepository.save(statusBuilder.status(STARTED).build());

        try {
            coreClient.reserveAmount(booking.getBookingId(), cardId, seat.price());
            bookingStatusRepository.save(statusBuilder.status(AMOUNT_RESERVED).build());

            var reservationId = partnerClient.reserveSeat(event.eventId(), seatId);
            bookingStatusRepository.save(statusBuilder.status(SEAT_BOOKED).build());

            coreClient.completePayment(booking.getBookingId(), partnerId);
            bookingStatusRepository.save(statusBuilder.status(PAYMENT_COMPLETED).build());

            log.info("booking {} completed successfully", booking.getBookingId());

            return reservationId;
        } catch (PaymentException pe) {
            bookingStatusRepository.save(statusBuilder.status(pe.getBookingState()).build());
            log.warn("booking {} failed: {}", booking.getBookingId(), pe.getBookingState());
            try {
                coreClient.revertTransaction(booking.getBookingId(), cardId);
                bookingStatusRepository.save(statusBuilder.status(PAYMENT_REVERTED).build());
                throw pe;
            } catch (PaymentException revertEx) {
                bookingStatusRepository.save(statusBuilder.status(revertEx.getBookingState()).build());
                log.error("failed to revert transaction {}", booking.getBookingId());
                throw revertEx;
            }
        }
    }
}
