package com.example.eric.combee.moviepicker.controller;

import com.example.eric.combee.moviepicker.model.request.ActorRequest;
import com.example.eric.combee.moviepicker.model.request.MovieSearch;
import com.example.eric.combee.moviepicker.services.ActorDetails;
import com.example.eric.combee.moviepicker.services.MovieDetails;
import com.example.eric.combee.moviepicker.utility.LoggingUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController implements MovieApi {

    @Autowired
    private MovieDetails movieDetails;

    @Autowired
    private ActorDetails actorDetails;

    @Autowired
    private LoggingUtility loggingUtility;


    @Override
    public ResponseEntity<?> getMovieDetails(String movieId) throws JsonProcessingException {
        loggingUtility.logInfo(null, "Entered into the controller with movieId: " + movieId);

        return new ResponseEntity<>(movieDetails.gatherMovieDetails(movieId).block(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getActorDetails(ActorRequest request) throws JsonProcessingException {
        String name = request.getFirstName() + " " + request.getLastName();
        loggingUtility.logInfo(null, "Entered into the controller for actor: " + name);
        return new ResponseEntity<>(actorDetails.gatherActorDetails(request).block(), HttpStatus.OK);
    }

}
