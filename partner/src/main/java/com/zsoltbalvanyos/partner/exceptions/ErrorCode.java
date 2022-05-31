package com.zsoltbalvanyos.partner.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    EVENT_NOT_FOUND(90001, "Nem létezik ilyen esemény"),
    SEAT_NOT_FOUND(90002, "Nem létezik ilyen szék!"),
    SEAT_RESERVED(90010, "Már lefoglalt székre nem lehet jegyet eladni!"),
    UNKNOWN_ERROR(99999, "Ismeretlen eredetű hiba");

    private final int code;
    private final String message;

}
