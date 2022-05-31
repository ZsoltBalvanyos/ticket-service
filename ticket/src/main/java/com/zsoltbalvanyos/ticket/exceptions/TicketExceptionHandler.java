package com.zsoltbalvanyos.ticket.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
@Slf4j
public class TicketExceptionHandler {

    @ExceptionHandler(value = {TicketException.class})
    public ResponseEntity<ErrorResponse> handleTicketException(TicketException ticketException) {

        log.error(ticketException.getErrorCode().getMessage());

        var status = switch (ticketException.getErrorCode()) {
            case SEAT_RESERVED -> HttpStatus.CONFLICT;
            case EVENT_NOT_FOUND, SEAT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.BAD_REQUEST;
        };

        return new ResponseEntity<>(
            new ErrorResponse(ticketException.getErrorCode().getCode()),
            status
        );
    }

    @ExceptionHandler(value = {PaymentException.class})
    public ResponseEntity<ErrorResponse> handlePaymentException(PaymentException paymentException) {
        log.error("failed to process booking {} due to {}", paymentException.bookingId, paymentException.bookingState);
        return new ResponseEntity<>(
            new ErrorResponse(ErrorCode.PAYMENT_FAILED.getCode()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {ResourceAccessException.class})
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException resourceAccessException) {
        log.error(resourceAccessException.getLocalizedMessage());
        return new ResponseEntity<>(
            new ErrorResponse(ErrorCode.EXTERNAL_SERVER_NOT_AVAILABLE.getCode()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(exception.getLocalizedMessage());
        return new ResponseEntity<>(
            new ErrorResponse(ErrorCode.UNKNOWN_ERROR.getCode()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
