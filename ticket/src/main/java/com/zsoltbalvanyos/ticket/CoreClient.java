package com.zsoltbalvanyos.ticket;

import java.math.BigDecimal;

public interface CoreClient {

    /**
     * Reserves the given amount for a transaction.
     *
     * If the transaction was successful, the {@link #completePayment(long, long)} (long, long)} method will finalize it.
     *
     * In case the transaction fails the {@link #revertTransaction(long, long, long)} (long, long, long)} method has to be
     * invoked by the transaction's initiator.
     *
     * @param amount the amount of the transaction
     * @param transactionId the id of the transaction
     * @param userId the id of the debited user
     * @param cardId the card id of the debited user
     */
    void reserveAmount(long transactionId, long userId, long cardId, BigDecimal amount);

    /**
     * Completes a transaction by deleting the reservation and crediting the partner
     *
     * @param transactionId the id of the transaction
     * @param partnerId the id of the credited partner
     */
    void completePayment(long transactionId, long partnerId);

    /**
     * Reverts an initiated transaction by deleting the reservation record and crediting the user's balance
     *
     * @param transactionId the id fo the transaction
     * @param userId the id of the user
     * @param cardId the card id of the user
     */
    void revertTransaction(long transactionId, long userId, long cardId);

}
