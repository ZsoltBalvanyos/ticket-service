package com.zsoltbalvanyos.core;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest
@Disabled
class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void reserve() throws Exception {
        mvc.perform(post("/payment/reserve")
                .queryParam("cardId", "1234")
                .queryParam("amount", "1452.95"))
            .andExpect(content().string(containsString("amount of 1452.95 from card 1234 has been reserved")));
    }

    @Test
    void complete() throws Exception {
        mvc.perform(post("/payment/complete")
                .queryParam("cardId", "1234")
                .queryParam("amount", "1452.95"))
            .andExpect(content().string(containsString("amount of 1452.95 from card 1234 has been withdrawn")));
    }

}
