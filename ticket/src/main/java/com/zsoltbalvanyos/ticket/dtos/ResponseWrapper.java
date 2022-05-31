package com.zsoltbalvanyos.ticket.dtos;

public record ResponseWrapper<T> (T data, boolean success) {}
