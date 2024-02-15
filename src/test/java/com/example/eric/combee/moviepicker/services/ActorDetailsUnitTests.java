package com.example.eric.combee.moviepicker.services;

import com.example.eric.combee.moviepicker.model.request.ActorRequest;
import com.example.eric.combee.moviepicker.model.response.ActorResponse;
import com.example.eric.combee.moviepicker.model.response.ActorSearchWebResponse;
import com.example.eric.combee.moviepicker.utility.LoggingUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ActiveProfiles("test")
class ActorDetailsUnitTests {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LoggingUtility loggingUtility;

    private ActorSearchWebResponse actorSearchWebResponse;
    @Autowired
    private ActorDetails actorDetails;

    private transient MockWebServer mockWebServer;
    @Value("${web.client.key}")
    private String key;

    private ActorRequest actorRequest;


    @BeforeAll
    void init() throws IOException {
        this.mockWebServer = new MockWebServer();
        mockWebServer.start(9095);

        actorSearchWebResponse = objectMapper.readValue(
                new ClassPathResource("ActorSearchResults.json").getInputStream(),
                ActorSearchWebResponse.class);

        actorRequest = objectMapper.readValue(new ClassPathResource("ActorSearch.json").getInputStream(), ActorRequest.class);

    }

    @AfterAll
    void shutDown() throws IOException {
        mockWebServer.shutdown();
    }


    @Test
    @Tag("unitTests")
    void gatherActorDetailsSuccessTest() throws JsonProcessingException {

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .setHeader(HttpHeaders.AUTHORIZATION, key)
                .setBody(objectMapper.writeValueAsString(actorSearchWebResponse)));

        actorDetails.gatherActorDetails(actorRequest);

        verify(loggingUtility).logInfo(any(ActorResponse.class), eq("Successfully mapped new actor request"));


    }
}