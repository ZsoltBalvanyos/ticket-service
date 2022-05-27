package com.zsoltbalvanyos.ticket.partners.besttickets.dtos;

import java.math.BigDecimal;

public record BestTicketSeat(
    long seatId,
    BigDecimal price,
    String currency,
    boolean reserved
){ }
