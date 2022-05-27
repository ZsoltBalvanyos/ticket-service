package com.zsoltbalvanyos.ticket.dtos;

import java.util.List;

public record EventDetails(long eventId, List<EventSeat> seats) { }
