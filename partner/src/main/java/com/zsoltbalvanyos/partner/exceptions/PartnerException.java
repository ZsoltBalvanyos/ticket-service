package com.zsoltbalvanyos.partner.exceptions;

import lombok.Value;

@Value
public class PartnerException extends RuntimeException {

    ErrorCode errorCode;
}
