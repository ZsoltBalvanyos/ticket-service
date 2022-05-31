package com.zsoltbalvanyos.api;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest
@WireMockTest(httpPort = 9992)
@Disabled
class ApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void validateUser() throws Exception {
        stubFor(WireMock.get("/user/validate").willReturn(aResponse().withStatus(500).withBody("{errorCode: 500}")));
        mvc.perform(get("/getEvents"))
            .andExpect(content().string(containsString("{errorCode: 500}")));
    }

    @Test
    void getEvents() throws Exception {
        stubFor(WireMock.get("/getEvents").willReturn(aResponse().withBody("get all events")));
        mvc.perform(get("/getEvents"))
            .andExpect(content().string(containsString("get all events")));
    }

    @Test
    void getEvent() throws Exception {
        stubFor(WireMock.get("/getEvent?eventId=1234").willReturn(aResponse().withBody("get event 1234")));
        mvc.perform(get("/getEvent")
                .queryParam("eventId", "1234"))
            .andExpect(content().string(containsString("get event 1234")));
    }

    @Test
    void pay() throws Exception {
        stubFor(WireMock.post("/pay?eventId=1234&seatId=8765").willReturn(aResponse().withBody("seat 8765 for event 1234 has been reserved")));
        mvc.perform(post("/pay")
                .queryParam("eventId", "1234")
                .queryParam("seatId", "8765"))
            .andExpect(content().string(containsString("seat 8765 for event 1234 has been reserved")));
    }

    @Test
    void notFoundPost() throws Exception {
        stubFor(WireMock.post(UrlPattern.ANY).willReturn(WireMock.notFound().withBody("not found")));
        mvc.perform(post("/xyz"))
            .andExpect(content().string(containsString("not found")));
    }

}
