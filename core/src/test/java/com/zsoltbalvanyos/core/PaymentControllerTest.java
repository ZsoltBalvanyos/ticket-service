package com.zsoltbalvanyos.core;

import com.zsoltbalvanyos.core.controllers.PaymentController;
import com.zsoltbalvanyos.core.services.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PaymentController.class)
@RunWith(SpringRunner.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void reserve() throws Exception {
        long transactionId = 1111;
        long userId = 2222;
        long cardId = 3333;
        BigDecimal amount = BigDecimal.valueOf(3000);

        mvc.perform(post("/payment/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("transactionId", String.valueOf(transactionId))
                .queryParam("cardId", String.valueOf(cardId))
                .queryParam("userId", String.valueOf(userId))
                .content("{\"amount\": 3000}"))
            .andExpect(content().string(""));

        verify(paymentService).reserve(transactionId, userId, cardId, amount);
    }

    @Test
    void complete() throws Exception {
        mvc.perform(post("/payment/complete")
                .queryParam("transactionId", "1234")
                .queryParam("partnerId", "2345"))
            .andExpect(content().string(""));

        verify(paymentService).complete(1234, 2345);
    }

    @Test
    void revert() throws Exception {
        mvc.perform(post("/payment/revert")
                .queryParam("transactionId", "111")
                .queryParam("userId", "222")
                .queryParam("cardId", "333"))
            .andExpect(content().string(""));

        verify(paymentService).revert(111, 222, 333);
    }

}
