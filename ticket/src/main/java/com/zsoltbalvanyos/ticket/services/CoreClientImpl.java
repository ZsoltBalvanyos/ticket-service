package com.zsoltbalvanyos.ticket.services;

import com.zsoltbalvanyos.ticket.CoreClient;
import com.zsoltbalvanyos.ticket.dtos.TransactionDetails;
import com.zsoltbalvanyos.ticket.entities.BookingState;
import com.zsoltbalvanyos.ticket.exceptions.AmountReservationException;
import com.zsoltbalvanyos.ticket.exceptions.PaymentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
@Slf4j
public class CoreClientImpl implements CoreClient {

    private final RestTemplate restTemplate;
    private final String endpoint;

    public CoreClientImpl(RestTemplate restTemplate, @Value("${core.endpoint}") String endpoint) {
        this.restTemplate = restTemplate;
        this.endpoint = endpoint + "/payment";
    }

    @Override
    public void reserveAmount(long transactionId, long userId, long cardId, BigDecimal amount) {
        var url = String.format(
            "%s/reserve?transactionId=%d&userId=%d&cardId=%d",
            endpoint,
            transactionId,
            userId,
            cardId);

        try {
            restTemplate.postForEntity(url, new TransactionDetails(amount), Void.class);
        } catch (RestClientResponseException e){
            throw new AmountReservationException(transactionId, BookingState.AMOUNT_RESERVATION_FAILED);
        }
    }

    @Override
    public void completePayment(long transactionId, long partnerId) {
        var url = String.format(
            "%s/complete?transactionId=%d&partnerId=%d",
            endpoint,
            transactionId,
            partnerId);

        try {
            restTemplate.postForEntity(url, Void.class, Void.class);
        } catch (RestClientResponseException e) {
            throw new PaymentException(transactionId, BookingState.PAYMENT_COMPLETION_FAILED);
        }
    }

    @Override
    public void revertTransaction(long transactionId, long userId, long cardId) {
        var url = String.format(
            "%s/revert?transactionId=%d&userId=%d&cardId=%d",
            endpoint,
            transactionId,
            userId,
            cardId);

        try {
            restTemplate.postForEntity(url, Void.class, Void.class);
        } catch (RestClientResponseException e) {
            throw new PaymentException(transactionId, BookingState.SEAT_BOOKING_FAILED);
        }
    }
}
