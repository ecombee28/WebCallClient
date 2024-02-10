package com.example.eric.combee.moviepicker.controller;

import com.example.eric.combee.moviepicker.model.request.ActorRequest;
import com.example.eric.combee.moviepicker.model.request.MovieSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


public interface MovieApi {

    @GetMapping(value = "/v1/movie/details/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getMovieDetails(@Parameter(in = ParameterIn.PATH, description = "", required = true) @PathVariable("movieId") String id) throws JsonProcessingException;

    @GetMapping(value = "v1/movie/actor-details", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getActorDetails(@Valid @RequestBody ActorRequest request) throws JsonProcessingException;

    @GetMapping(value = "v1/movie/search", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> searchForMovie(@RequestHeader String date, @Valid @RequestBody MovieSearch body);
}
