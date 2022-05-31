package com.zsoltbalvanyos.partner.dtos;

public record ResponseWrapper<T> (T data, boolean success) {}
