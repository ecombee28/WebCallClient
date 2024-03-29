package com.example.eric.combee.moviepicker.controller;

import com.example.eric.combee.moviepicker.model.request.ActorRequest;
import com.example.eric.combee.moviepicker.model.request.MovieSearchRequest;
import com.example.eric.combee.moviepicker.model.response.moviesearch.MovieDetailModel;
import com.example.eric.combee.moviepicker.services.ActorDetails;
import com.example.eric.combee.moviepicker.services.MovieCast;
import com.example.eric.combee.moviepicker.services.MovieDetails;
import com.example.eric.combee.moviepicker.services.MovieSearch;
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
    @Autowired
    private MovieSearch movieSearch;

    @Autowired
    private MovieCast movieCast;


    @Override
    public ResponseEntity<?> getMovieDetails(String movieId) throws JsonProcessingException {
        loggingUtility.logInfo(null, "Entered into the controller with movieId: " + movieId);

        MovieDetailModel movieDetailModel = movieDetails.gatherMovieDetails(movieId);
        movieDetailModel.setCastList(movieCast.getMovieCast(movieId));

        return new ResponseEntity<>(movieDetailModel, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getActorDetails(ActorRequest request) throws JsonProcessingException {
        String name = request.getFirstName() + " " + request.getLastName();
        loggingUtility.logInfo(request, "Entered into the controller for actor: " + name);
        return new ResponseEntity<>(actorDetails.gatherActorDetails(request), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchForMovie(MovieSearchRequest body) throws JsonProcessingException {
        loggingUtility.logInfo(body, "Entered into the controller for movie: " + body.getMovieName());
        return new ResponseEntity<>(movieSearch.searchForMovie(body), HttpStatus.OK);
    }

}
