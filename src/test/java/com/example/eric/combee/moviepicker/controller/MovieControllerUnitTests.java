package com.example.eric.combee.moviepicker.controller;

import com.example.eric.combee.moviepicker.model.request.ActorRequest;
import com.example.eric.combee.moviepicker.model.request.MovieSearchRequest;
import com.example.eric.combee.moviepicker.utility.LoggingUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ActiveProfiles("test")
class MovieControllerUnitTests {

    private final String MOVIE_DETAIL_URL = "/v1/details/movie/9489";
    private final String ACTOR_DETAIL_URL = "/v1/details/actor";
    private final String MOVIE_SEARCH_URL = "/v1/details/movie";
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private LoggingUtility loggingUtility;
    @Autowired
    private ObjectMapper objectMapper;

    private ActorRequest actorRequest;

    private MovieSearchRequest movieSearchRequest;

    @BeforeAll
    void init() throws IOException {

        actorRequest = objectMapper.readValue(new ClassPathResource("ActorSearch.json").getInputStream(), ActorRequest.class);
        movieSearchRequest = objectMapper.readValue(new ClassPathResource("MovieSearch.json").getInputStream(), MovieSearchRequest.class);
    }

    @Test
    @Tag("unitTests")
    void getMovieDetailsSuccessTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(MOVIE_DETAIL_URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        verify(loggingUtility).logInfo(any(), eq("Entered into the controller with movieId: 9489"));


    }

    @Test
    @Tag("unitTests")
    void getActorDetailsSuccessTest() throws Exception {

        String name = actorRequest.getFirstName() + " " + actorRequest.getLastName();
        mockMvc.perform(MockMvcRequestBuilders.get(ACTOR_DETAIL_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actorRequest)))
                .andExpect(status().isOk());

        verify(loggingUtility).logInfo(any(), eq("Entered into the controller for actor: " + name));

    }

    @Test
    @Tag("unitTests")
    void searchForMovieSuccessTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(MOVIE_SEARCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieSearchRequest)))
                .andExpect(status().isOk());

        verify(loggingUtility).logInfo(any(), eq("Entered into the controller for movie: " + movieSearchRequest.getMovieName()));

    }
}