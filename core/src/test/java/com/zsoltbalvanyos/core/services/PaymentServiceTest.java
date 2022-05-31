package com.zsoltbalvanyos.core.services;

import com.zsoltbalvanyos.core.entities.PartnerBalance;
import com.zsoltbalvanyos.core.entities.ReservedAmount;
import com.zsoltbalvanyos.core.entities.UserBankCard;
import com.zsoltbalvanyos.core.exceptions.CoreException;
import com.zsoltbalvanyos.core.repositories.PartnerRepository;
import com.zsoltbalvanyos.core.repositories.ReservedAmountRepository;
import com.zsoltbalvanyos.core.repositories.UserBankCardRepository;
import com.zsoltbalvanyos.core.testutils.DatabaseCleanup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static com.zsoltbalvanyos.core.utils.PrefixBuilder.prefixCardId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@TestExecutionListeners(value = {DatabaseCleanup.class, DependencyInjectionTestExecutionListener.class})
class PaymentServiceTest {

    @Autowired
    private UserBankCardRepository userBankCardRepository;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private ReservedAmountRepository reservedAmountRepository;

    @Autowired
    private PaymentService paymentService;

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:14.3-alpine3.15")
        .withDatabaseName("core")
        .withUsername("foo")
        .withPassword("secret")
        .withExposedPorts(5432);

    private final static long transactionId = 123;
    private final static long userId = 1000;
    private final static long cardId = 1;
    private final static long partnerId = 1;
    private final static String prefixedCardId = prefixCardId(cardId);

    @Test
    void shouldReserveAmount() {
        long price = 4;

        paymentService.reserve(transactionId, userId, cardId, BigDecimal.valueOf(price));

        var result = userBankCardRepository.findById(prefixedCardId).get();
        assertThat(result.getAmount().longValue()).isEqualTo(996);
        assertThat(reservedAmountRepository.findById(transactionId)).isPresent();
    }

    @Test
    void shouldThrowCoreExceptionWhenBalanceExceeded() {
        long price = 4000;

        assertThatThrownBy(() -> paymentService.reserve(transactionId, userId, cardId, BigDecimal.valueOf(price)))
            .isInstanceOf(CoreException.class);

        var result = userBankCardRepository.findById(prefixedCardId).get();
        assertThat(result.getAmount().longValue()).isEqualTo(1000);
        assertThat(reservedAmountRepository.findById(transactionId)).isEmpty();
    }

    @Test
    void shouldCompleteTransaction() {
        long transactionId = 111;
        long amount = 500;

        reservedAmountRepository.save(
            ReservedAmount.builder()
                .transactionId(transactionId)
                .cardId(prefixedCardId)
                .amount(BigDecimal.valueOf(amount))
                .build()
        );

        paymentService.complete(transactionId, partnerId);

        assertThat(reservedAmountRepository.findById(transactionId)).isEmpty();
        assertThat(partnerRepository.findById(partnerId))
            .map(PartnerBalance::getAmount)
            .map(BigDecimal::longValue)
            .contains(80500L);
    }

    @Test
    void shouldRevertTransaction() {
        long transactionId = 111;
        long amount = 500;

        reservedAmountRepository.save(
            ReservedAmount.builder()
                .transactionId(transactionId)
                .cardId(prefixedCardId)
                .amount(BigDecimal.valueOf(amount))
                .build()
        );

        paymentService.revert(transactionId, userId, cardId);

        assertThat(reservedAmountRepository.findById(transactionId)).isEmpty();
        assertThat(userBankCardRepository.findById(prefixedCardId))
            .map(UserBankCard::getAmount)
            .map(BigDecimal::longValue)
            .contains(1500L);
    }

    @Test
    void shouldThrowCoreExceptionWhenTransactionNotFound() {
        long transactionId = 111;

        assertThatThrownBy(() -> paymentService.complete(transactionId, partnerId))
            .isInstanceOf(CoreException.class);

        assertThatThrownBy(() -> paymentService.revert(transactionId, userId, cardId))
            .isInstanceOf(CoreException.class);

        assertThat(partnerRepository.findById(partnerId))
            .map(PartnerBalance::getAmount)
            .map(BigDecimal::longValue)
            .contains(80000L);
    }
}