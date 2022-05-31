package com.zsoltbalvanyos.ticket.exceptions;

import lombok.Value;

@Value
public class TicketException extends RuntimeException {
    ErrorCode errorCode;

    public TicketException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}