package com.zsoltbalvanyos.ticket.dtos;

public record EventSummary(
    long eventId,
    String title,
    String location,
    long startTimestamp,
    long endTimestamp
) { }
