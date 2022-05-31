package com.zsoltbalvanyos.core.dtos;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TransactionDetails(
    @NotNull(message = "Amount of reservation must be provided")
    BigDecimal amount
) {
}
