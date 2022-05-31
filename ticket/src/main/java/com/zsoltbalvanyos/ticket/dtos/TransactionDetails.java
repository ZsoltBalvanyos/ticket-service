package com.zsoltbalvanyos.ticket.dtos;

import java.math.BigDecimal;

public record TransactionDetails(
    BigDecimal amount
) {
}
