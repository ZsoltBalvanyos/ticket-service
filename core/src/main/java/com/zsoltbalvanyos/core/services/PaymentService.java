package com.zsoltbalvanyos.core.services;

import com.zsoltbalvanyos.core.entities.ReservedAmount;
import com.zsoltbalvanyos.core.exceptions.CardNotFoundException;
import com.zsoltbalvanyos.core.exceptions.DuplicatedReservationException;
import com.zsoltbalvanyos.core.exceptions.PartnerNotFoundException;
import com.zsoltbalvanyos.core.exceptions.TransactionNotReservedException;
import com.zsoltbalvanyos.core.repositories.PartnerRepository;
import com.zsoltbalvanyos.core.repositories.ReservedAmountRepository;
import com.zsoltbalvanyos.core.repositories.UserBankCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final UserBankCardRepository userBankCardRepository;
    private final PartnerRepository partnerRepository;
    private final ReservedAmountRepository reservedAmountRepository;

    @Transactional
    public void reserve(long transactionId, long cardId, BigDecimal amount) {

        if (reservedAmountRepository.existsById(transactionId)) {
            throw new DuplicatedReservationException();
        }

        var prefixedCardId = prefixCardId(cardId);

        reservedAmountRepository.save(
            ReservedAmount.builder()
                .transactionId(transactionId)
                .cardId(prefixedCardId)
                .amount(amount)
                .build()
        );

        var userBankCard = userBankCardRepository
            .findAndLockByCardId(prefixedCardId)
            .orElseThrow(CardNotFoundException::new);

        userBankCard.setAmount(userBankCard.getAmount().subtract(amount));
    }

    @Transactional
    public void complete(long transactionId, long partnerId) {
        var reservedAmount = reservedAmountRepository
            .findAndLockByTransactionId(transactionId)
            .orElseThrow(TransactionNotReservedException::new);

        reservedAmountRepository.deleteById(transactionId);

        var partner = partnerRepository
            .findAndLockByPartnerId(partnerId)
            .orElseThrow(PartnerNotFoundException::new);

        partner.setAmount(partner.getAmount().add(reservedAmount.getAmount()));
    }

    @Transactional
    public void revert(long transactionId, long cardId) {
        var reservedAmount = reservedAmountRepository
            .findAndLockByTransactionId(transactionId)
            .orElseThrow(TransactionNotReservedException::new);

        reservedAmountRepository.deleteById(transactionId);

        var userBankCard = userBankCardRepository
            .findAndLockByCardId(prefixCardId(cardId))
            .orElseThrow(CardNotFoundException::new);

        userBankCard.setAmount(userBankCard.getAmount().add(reservedAmount.getAmount()));
    }

    private String prefixCardId(long cardId) {
        return "C000" + cardId;
    }

}
