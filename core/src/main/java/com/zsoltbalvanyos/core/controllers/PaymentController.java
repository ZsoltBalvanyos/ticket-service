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

    /**
     * Reserves the given amount for a transaction.
     *
     * If the transaction was successful, the {@link #complete(long, long)} method will finalize it.
     *
     * In case the transaction fails the {@link #revert(long, long, long)} method has to be
     * invoked by the transaction's initiator.
     *
     * @param transactionDetails information about the transaction
     * @param transactionId the id of the transaction
     * @param userId the id of the debited user
     * @param cardId the card id of the debited user
     */
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

    /**
     * Completes a transaction by deleting the reservation and crediting the partner
     *
     * @param transactionId the id of the transaction
     * @param partnerId the id of the credited partner
     */
    @PostMapping("/complete")
    public void complete(
        @RequestParam("transactionId") long transactionId,
        @RequestParam("partnerId") long partnerId
    ) {
        paymentService.complete(transactionId, partnerId);
    }

    /**
     * Reverts an initiated transaction by deleting the reservation record and crediting the user's balance
     *
     * @param transactionId the id fo the transaction
     * @param userId the id of the user
     * @param cardId the card id of the user
     */
    @PostMapping("/revert")
    public void revert(
        @RequestParam("transactionId") long transactionId,
        @RequestParam("userId") long userId,
        @RequestParam("cardId") long cardId
    ) {
        paymentService.revert(transactionId, userId, cardId);
    }
}
