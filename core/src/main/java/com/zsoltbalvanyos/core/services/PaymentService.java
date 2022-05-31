package com.zsoltbalvanyos.core.services;

import com.zsoltbalvanyos.core.entities.ReservedAmount;
import com.zsoltbalvanyos.core.exceptions.CoreException;
import com.zsoltbalvanyos.core.exceptions.ErrorCode;
import com.zsoltbalvanyos.core.repositories.PartnerRepository;
import com.zsoltbalvanyos.core.repositories.ReservedAmountRepository;
import com.zsoltbalvanyos.core.repositories.UserBankCardRepository;
import com.zsoltbalvanyos.core.utils.PrefixBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentService {

    private final UserBankCardRepository userBankCardRepository;
    private final PartnerRepository partnerRepository;
    private final ReservedAmountRepository reservedAmountRepository;

    @Transactional
    public void reserve(long transactionId, long userId, long cardId, BigDecimal amount) {

        if (reservedAmountRepository.existsById(transactionId)) {
            throw new CoreException(ErrorCode.DUPLICATED_RESERVATION);
        }

        var prefixedCardId = PrefixBuilder.prefixCardId(cardId);

        var userBankCard = userBankCardRepository
            .findAndLockByCardId(prefixedCardId)
            .orElseThrow(() -> new CoreException(ErrorCode.CARD_NOT_FOUND));

        if (userBankCard.getUserId() != userId) {
            throw new CoreException(ErrorCode.USER_ID_CARD_ID_MISMATCH);
        }

        if (userBankCard.getAmount().compareTo(amount) < 0) {
            throw new CoreException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        reservedAmountRepository.save(
            ReservedAmount.builder()
                .transactionId(transactionId)
                .cardId(prefixedCardId)
                .amount(amount)
                .build()
        );

        userBankCard.setAmount(userBankCard.getAmount().subtract(amount));
    }

    @Transactional
    public void complete(long transactionId, long partnerId) {
        var reservedAmount = reservedAmountRepository
            .findAndLockByTransactionId(transactionId)
            .orElseThrow(() -> new CoreException(ErrorCode.TRANSACTION_NOT_RESERVED));

        reservedAmountRepository.deleteById(transactionId);

        var partner = partnerRepository
            .findAndLockByPartnerId(partnerId)
            .orElseThrow(() -> new CoreException(ErrorCode.PARTNER_NOT_FOUND));

        partner.setAmount(partner.getAmount().add(reservedAmount.getAmount()));
    }

    @Transactional
    public void revert(long transactionId, long userId, long cardId) {
        var reservedAmount = reservedAmountRepository
            .findAndLockByTransactionId(transactionId)
            .orElseThrow(() -> new CoreException(ErrorCode.TRANSACTION_NOT_RESERVED));

        reservedAmountRepository.deleteById(transactionId);

        var userBankCard = userBankCardRepository
            .findAndLockByCardId(PrefixBuilder.prefixCardId(cardId))
            .orElseThrow(() -> new CoreException(ErrorCode.CARD_NOT_FOUND));

        if (userBankCard.getUserId() != userId) {
            throw new CoreException(ErrorCode.USER_ID_CARD_ID_MISMATCH);
        }

        userBankCard.setAmount(userBankCard.getAmount().add(reservedAmount.getAmount()));
    }

}
