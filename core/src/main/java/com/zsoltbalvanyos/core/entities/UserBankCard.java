package com.zsoltbalvanyos.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBankCard {

    @Id
    private String cardId;

    private long userId;
    private long cardNumber;
    private short cvc;
    private String name;
    private BigDecimal amount;
    private String currency;
}
