package com.zsoltbalvanyos.core.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CoreExceptionHandler {

    @ExceptionHandler(value = {CoreException.class})
    public ResponseEntity<ErrorResponse> handleException(CoreException coreException) {

        log.error(coreException.getErrorCode().getMessage());

        var status = switch (coreException.getErrorCode()) {
            case TRANSACTION_NOT_RESERVED, DUPLICATED_RESERVATION -> HttpStatus.CONFLICT;
            case TOKEN_NOT_FOUND -> HttpStatus.UNAUTHORIZED;
            default -> HttpStatus.BAD_REQUEST;
        };

        return new ResponseEntity<>(
            new ErrorResponse(coreException.getErrorCode().getCode()),
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
