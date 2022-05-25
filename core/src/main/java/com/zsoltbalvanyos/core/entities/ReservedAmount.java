package com.zsoltbalvanyos.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservedAmount {

    @Id
    private String transactionId;

    private long cardId;
    private BigDecimal amount;
    private Instant reservedAt;
}
