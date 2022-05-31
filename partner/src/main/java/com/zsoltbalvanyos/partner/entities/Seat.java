package com.zsoltbalvanyos.partner.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue
    private long reservationId;

    private long seatId;
    private long eventId;
    private BigDecimal price;
    private String currency;
    private boolean reserved;

    @Version
    private int version;
}
