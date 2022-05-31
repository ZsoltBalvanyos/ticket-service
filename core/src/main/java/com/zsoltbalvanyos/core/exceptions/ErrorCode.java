package com.zsoltbalvanyos.core.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    CARD_NOT_FOUND(10035, "Nem létezik ilyen kártya"),
    DUPLICATED_RESERVATION(10062, "A tranzakcióhoz szükzéges összeg már lefoglalásra került"),
    PARTNER_NOT_FOUND(10038, "Nem létezik ilyen partner"),
    TRANSACTION_NOT_RESERVED(10092, "A tranzakcióhoz nem tartozik lefoglalt összeg"),
    INSUFFICIENT_FUNDS(10101, "A felhasználónak nincs elegendő pénze hogy megvásárolja a jegyet!"),
    TOKEN_NOT_FOUND(10050, "A felhasználó token nem szerepel"),
    INVALID_TOKEN(10051, "A felhasználói token lejárt vagy nem értelmezhető"),
    USER_NOT_FOUND(10112, "Nem létezik ilyen felhasználó"),
    USER_ID_EMAIL_MISMATCH(10115, "Nem létezik ilyen felhasználó ilyen email címmel"),
    USER_ID_CARD_ID_MISMATCH(10116, "A megadott kártya nem a felhasználóhoz tartozik"),
    UNREGISTERED_DEVICE(10117, "A kérés nem regisztrált készülékről érkezett"),
    UNKNOWN_ERROR(19999, "Ismeretlen eredetű hiba");

    private final int code;
    private final String message;

}