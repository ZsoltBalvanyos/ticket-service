package com.zsoltbalvanyos.core.exceptions;

public class UserException extends RuntimeException {

    private final Error error;

    public UserException(Error error) {
        this.error = error;
    }

    public enum Error {
        USER_ID_NOT_FOUND,
        USER_ID_EMAIL_ADDRESS_MISMATCH,
        UNREGISTERED_DEVICE;
    }
}
