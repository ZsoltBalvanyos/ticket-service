package com.zsoltbalvanyos.partner.dtos;

import java.util.List;

public record EventDetails(long eventId, List<EventSeat> seats) { }
