package com.zsoltbalvanyos.ticket.exceptions;

import lombok.Value;

@Value
public class ErrorResponse {
    int errorCode;
    boolean success = false;
}
