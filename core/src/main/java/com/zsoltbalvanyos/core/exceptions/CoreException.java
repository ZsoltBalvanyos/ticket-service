package com.zsoltbalvanyos.core.exceptions;

import lombok.Value;

@Value
public class CoreException extends RuntimeException {

    ErrorCode errorCode;

}
