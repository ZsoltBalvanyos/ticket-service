package com.zsoltbalvanyos.ticket.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    EVENT_NOT_FOUND(20001, "Nem létezik ilyen esemény"),
    SEAT_NOT_FOUND(20002, "Nem létezik ilyen szék!"),
    SEAT_RESERVED(20010, "Már lefoglalt székre nem lehet jegyet eladni!"),
    EVENT_IN_PAST(20011, "Olyan eseményre ami már elkezdődött, nem lehet jegyet kapni"),
    PARTNER_NOT_FOUND(20015, "Nem létezik ilyen partner!"),
    EXTERNAL_SERVER_NOT_AVAILABLE(20404, "A külső rendszer nem elérhető"),
    EXTERNAL_SERVER_ERROR(20405, "Külső rendszer hiba"),
    PAYMENT_FAILED(20500, "A tranzakció sikertelen"),
    UNKNOWN_ERROR(29999, "Ismeretlen eredetű hiba");

    private final int code;
    private final String message;

}
