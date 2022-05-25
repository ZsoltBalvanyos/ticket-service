package com.zsoltbalvanyos.core.controllers;

import com.zsoltbalvanyos.core.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/reserve")
    public void reserve(
        @RequestParam("transactionId") String transactionId,
        @RequestParam("cardId") long cardId,
        @RequestParam("amount") BigDecimal amount
    ) {
        paymentService.reserve(transactionId, cardId, amount);
    }

    @PostMapping("/complete")
    public void complete(
        @RequestParam("transactionId") String transactionId,
        @RequestParam("partnerId") long partnerId
    ) {
        paymentService.complete(transactionId, partnerId);
    }

    @PostMapping("/revert")
    public void revert(
        @RequestParam("transactionId") String transactionId,
        @RequestParam("cardId") long cardId
    ) {
        paymentService.revert(transactionId, cardId);
    }
}
