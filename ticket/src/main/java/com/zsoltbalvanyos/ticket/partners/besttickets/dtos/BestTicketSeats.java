package com.zsoltbalvanyos.ticket.partners.besttickets.dtos;

import java.util.List;

public record BestTicketSeats(long eventId, List<BestTicketSeat> seats) { }
