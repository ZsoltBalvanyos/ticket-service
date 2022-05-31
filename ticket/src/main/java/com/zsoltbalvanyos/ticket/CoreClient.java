package com.zsoltbalvanyos.ticket;

import java.math.BigDecimal;

public interface CoreClient {

    void reserveAmount(long transactionId, long userId, long cardId, BigDecimal amount);

    void completePayment(long transactionId, long partnerId);

    void revertTransaction(long transactionId, long userId, long cardId);

}
