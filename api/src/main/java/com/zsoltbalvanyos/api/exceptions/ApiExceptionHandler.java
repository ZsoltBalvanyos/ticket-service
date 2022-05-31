package com.zsoltbalvanyos.api.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ResourceAccessException.class})
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException resourceAccessException) {
        log.error(resourceAccessException.getLocalizedMessage());
        return new ResponseEntity<>(
            new ErrorResponse(ErrorCode.EXTERNAL_SERVER_ERROR.getCode()),
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
