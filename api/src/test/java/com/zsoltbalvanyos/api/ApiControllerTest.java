package com.zsoltbalvanyos.api;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.UrlPattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(ApiController.class)
@WireMockTest
class ApiControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final WireMockServer coreMockServer = new WireMockServer(9992);
    private static final WireMockServer ticketMockServer = new WireMockServer(9993);

    @BeforeAll
    public static void setup() {
        coreMockServer.start();
        ticketMockServer.start();
    }

    @Test
    void validateUser() throws Exception {
        coreMockServer.stubFor(WireMock.get("/user/validate").withPort(9992).willReturn(aResponse().withStatus(500).withBody("{errorCode: 500}")));
        mvc.perform(get("/getEvents"))
            .andExpect(content().string(containsString("{errorCode: 500}")));
    }

    @Test
    void getEvents() throws Exception {
        coreMockServer.stubFor(WireMock.get("/user/validate").withPort(9992).willReturn(aResponse().withStatus(200)));
        ticketMockServer.stubFor(WireMock.get("/getEvents").withPort(9993).willReturn(aResponse().withStatus(200).withBody("get all events")));
        mvc.perform(get("/getEvents"))
            .andExpect(content().string(containsString("get all events")));
    }

    @Test
    void getEvent() throws Exception {
        coreMockServer.stubFor(WireMock.get("/user/validate?eventId=1234").withPort(9992).willReturn(aResponse().withStatus(200)));
        ticketMockServer.stubFor(WireMock.get("/getEvent?eventId=1234").willReturn(aResponse().withBody("get event 1234")));
        mvc.perform(get("/getEvent")
                .queryParam("eventId", "1234"))
            .andExpect(content().string(containsString("get event 1234")));
    }

    @Test
    void pay() throws Exception {
        coreMockServer.stubFor(WireMock.get("/user/validate?eventId=1234&seatId=8765").withPort(9992).willReturn(aResponse().withStatus(200)));
        ticketMockServer.stubFor(WireMock.post("/pay?eventId=1234&seatId=8765").willReturn(aResponse().withBody("seat 8765 for event 1234 has been reserved")));
        mvc.perform(post("/pay")
                .queryParam("eventId", "1234")
                .queryParam("seatId", "8765"))
            .andExpect(content().string(containsString("seat 8765 for event 1234 has been reserved")));
    }

    @Test
    void notFoundPost() throws Exception {
        coreMockServer.stubFor(WireMock.get("/user/validate").withPort(9992).willReturn(aResponse().withStatus(200)));
        ticketMockServer.stubFor(WireMock.post(UrlPattern.ANY).willReturn(WireMock.notFound().withBody("not found")));
        mvc.perform(post("/xyz"))
            .andExpect(content().string(containsString("not found")));
    }

}
