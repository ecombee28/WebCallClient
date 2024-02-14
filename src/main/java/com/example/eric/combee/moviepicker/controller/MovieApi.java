package com.example.eric.combee.moviepicker.controller;

import com.example.eric.combee.moviepicker.model.request.ActorRequest;
import com.example.eric.combee.moviepicker.model.request.MovieSearchRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


public interface MovieApi {

    @GetMapping(value = "/v1/details/movie/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getMovieDetails(@Parameter(in = ParameterIn.PATH, description = "", required = true) @PathVariable("movieId") String id) throws JsonProcessingException;

    @GetMapping(value = "v1/details/actor", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getActorDetails(@Valid @RequestBody ActorRequest request) throws JsonProcessingException;

    @GetMapping(value = "v1/details/movie", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> searchForMovie(@Valid @RequestBody MovieSearchRequest body) throws JsonProcessingException;

}
