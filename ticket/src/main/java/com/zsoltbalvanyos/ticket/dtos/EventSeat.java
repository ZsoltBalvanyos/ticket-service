package com.zsoltbalvanyos.ticket.dtos;

import java.math.BigDecimal;

public record EventSeat(
    long seatId,
    BigDecimal price,
    String currency,
    boolean reserved
){ }
