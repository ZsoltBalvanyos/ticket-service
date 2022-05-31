package com.zsoltbalvanyos.partner.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class PartnerExceptionHandler {

    @ExceptionHandler(value = {PartnerException.class})
    public ResponseEntity<ErrorResponse> handleException(PartnerException partnerException) {

        log.error(partnerException.getErrorCode().getMessage());

        var status = switch (partnerException.getErrorCode()) {
            case SEAT_NOT_FOUND, EVENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case SEAT_RESERVED -> HttpStatus.CONFLICT;
            case UNKNOWN_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return new ResponseEntity<>(
            new ErrorResponse(partnerException.getErrorCode().getCode()),
            status
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
