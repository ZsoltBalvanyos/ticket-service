package com.zsoltbalvanyos.partner;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest
@Disabled
class PartnerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getEvents() throws Exception {
        mvc.perform(get("/getEvents"))
            .andExpect(content().string(containsString("get all events")));
    }

    @Test
    void getEvent() throws Exception {
        mvc.perform(get("/getEvent")
                .queryParam("eventId", "1234"))
            .andExpect(content().string(containsString("get event 1234")));
    }

    @Test
    void reserve() throws Exception {
        mvc.perform(post("/reserve")
                .queryParam("eventId", "1234")
                .queryParam("seatId", "8765"))
            .andExpect(content().string(containsString("seat 8765 for event 1234 has been reserved")));
    }

}
