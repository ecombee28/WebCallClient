package com.example.eric.combee.moviepicker.services;

import com.example.eric.combee.moviepicker.model.request.MovieSearchRequest;
import com.example.eric.combee.moviepicker.model.response.MovieRequest;
import com.example.eric.combee.moviepicker.model.response.MovieSearchModel;
import com.example.eric.combee.moviepicker.model.response.MovieSearchResponse;
import com.example.eric.combee.moviepicker.utility.LoggingUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieSearch extends ResponseException {

    @Autowired
    @Qualifier("webClientBase")
    private WebClient webClient;
    @Autowired
    private LoggingUtility loggingUtility;
    @Value("${web.retry.times}")
    private int maxAttempts;
    @Value("${web.retry.wait}")
    private int retryWaitTime;
    @Value("${web.client.key}")
    private String key;
    @Value("${tmdb.background.path.url}")
    private String backgroundPath;
    @Value("${tmdb.poster.path.url}")
    private String posterPath;
    @Value("${tmdb.path.movie.search}")
    private String url;

    public MovieSearchResponse searchForMovie(MovieSearchRequest request) {
        String movie = request.getMovieName();
        String searchYear = request.getReleaseYear();


        return webClient.get().
                uri(uriBuilder -> uriBuilder.path(url)
                        .queryParam("query", movie)
                        .queryParam("language", "en-US")
                        .queryParam("include_adult", "false")
                        .queryParam("year", searchYear)
                        .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, key)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::createResponseException)
                .onStatus(HttpStatusCode::is5xxServerError, this::createResponseException)
                .bodyToMono(MovieRequest.class)
                .retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofMillis(retryWaitTime))
                        .filter(throwable -> {
                            if (throwable instanceof WebClientResponseException) {
                                return ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
                            } else {
                                return false;
                            }
                        }))
                .map(response -> {
                    try {
                        return prepareResponse(response);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                })
                .onErrorResume(error -> {
                    loggingUtility.logWebClientError("There was an error calling TMDB for movie " + movie, error);
                    return Mono.error(error);
                })
                .block();


    }

    private MovieSearchResponse prepareResponse(MovieRequest body) throws JsonProcessingException {
        MovieSearchResponse movieSearchResponse = new MovieSearchResponse();
        List<MovieSearchModel> searchResults = body.getResults()
                .stream()
                .filter(vote -> vote.getVoteCount() > 2000)
                .map(results -> prepareResponse(results))
                .collect(Collectors.toList());

        movieSearchResponse.setResults(searchResults);
        movieSearchResponse.setTotalResults(searchResults.size());


        return movieSearchResponse;
    }


    private MovieSearchModel prepareResponse(MovieSearchModel response) {

        response.setBackgroundPath(backgroundPath + response.getBackgroundPath());
        response.setPosterPath(posterPath + response.getPosterPath());

        return response;

    }


}
