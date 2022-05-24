package com.zsoltbalvanyos.core;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @PostMapping("/reserve")
    public String reserve(
        @RequestParam("cardId") long cardId,
        @RequestParam("amount") BigDecimal amount
    ) {
        return String.format("amount of %s from card %d has been reserved", amount, cardId);
    }

    @PostMapping("/complete")
    public String complete(
        @RequestParam("cardId") long cardId,
        @RequestParam("amount") BigDecimal amount
    ) {
        return String.format("amount of %s from card %d has been withdrawn", amount, cardId);
    }
}
