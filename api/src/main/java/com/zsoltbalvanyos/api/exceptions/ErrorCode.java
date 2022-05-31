package com.zsoltbalvanyos.api.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    EXTERNAL_SERVER_ERROR(30405, "Külső rendszer hiba"),
    UNKNOWN_ERROR(39999, "Ismeretlen eredetű hiba");

    private final int code;
    private final String message;
}
