package com.zsoltbalvanyos.core.controllers;

import com.zsoltbalvanyos.core.dtos.TransactionDetails;
import com.zsoltbalvanyos.core.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/reserve")
    public void reserve(
        @Valid @RequestBody TransactionDetails transactionDetails,
        @RequestParam("transactionId") long transactionId,
        @RequestParam("userId") long userId,
        @RequestParam("cardId") long cardId
    ) {
        paymentService.reserve(
            transactionId,
            userId,
            cardId,
            transactionDetails.amount());
    }

    @PostMapping("/complete")
    public void complete(
        @RequestParam("transactionId") long transactionId,
        @RequestParam("partnerId") long partnerId
    ) {
        paymentService.complete(transactionId, partnerId);
    }

    @PostMapping("/revert")
    public void revert(
        @RequestParam("transactionId") long transactionId,
        @RequestParam("userId") long userId,
        @RequestParam("cardId") long cardId
    ) {
        paymentService.revert(transactionId, userId, cardId);
    }
}
