package com.example.eric.combee.moviepicker.services;

import com.example.eric.combee.moviepicker.model.request.MovieSearchRequest;
import com.example.eric.combee.moviepicker.model.response.MovieDetailModel;
import com.example.eric.combee.moviepicker.utility.LoggingUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.management.ServiceNotFoundException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieSearch {

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
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${tmdb.background.path.url}")
    private String backgroundPath;
    @Value("${tmdb.poster.path.url}")
    private String posterPath;

    public List<MovieDetailModel> searchForMovie(MovieSearchRequest request) {
        String movie = request.getMovieName();
        String searchYear = request.getReleaseDate() != null ? request.getReleaseDate() : "";

        //--url 'https://api.themoviedb.org/3/search/movie?query=batman&include_adult=false&language=en-US&page=1&year=1989' \

        return webClient.get().
                uri(uriBuilder -> uriBuilder.path("search/movie")
                        .queryParam("query", movie)
                        .queryParam("language", "en-US")
                        .queryParam("include_adult", "false")
                        .queryParam("year", searchYear)
                        .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, key)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new ServiceNotFoundException(movie + " not found")))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Internal error")))
                .bodyToMono(MovieDetailModel.class)
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
                        return prepareResponse(response, request);
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

    private List<MovieDetailModel> prepareResponse(MovieDetailModel body, MovieSearchRequest request) throws JsonProcessingException {
        List<MovieDetailModel> searchResults = new ArrayList<>();

        System.out.println(objectMapper.writeValueAsString(body));

        return searchResults;
    }
}
