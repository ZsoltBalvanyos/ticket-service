package com.zsoltbalvanyos.ticket.partners.besttickets.dtos;

public record BestTicketEvent(
    long eventId,
    String title,
    String location,
    long startTimestamp,
    long endTimestamp
) { }
