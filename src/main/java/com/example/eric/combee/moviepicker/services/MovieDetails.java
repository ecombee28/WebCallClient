package com.example.eric.combee.moviepicker.services;

import com.example.eric.combee.moviepicker.model.response.MovieDetailModel;
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

import java.text.NumberFormat;
import java.time.Duration;

@Service
public class MovieDetails extends ResponseException {

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
    @Value("${tmdb.path.movie.details}")
    private String url;


    public MovieDetailModel gatherMovieDetails(String movieId) {

        return webClient.get().
                uri(uriBuilder -> uriBuilder.path(url)
                        .queryParam("language", "en-US")
                        .build(movieId))
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, key)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::createResponseException)
                .onStatus(HttpStatusCode::is5xxServerError, this::createResponseException)
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
                        return prepareResponse(response);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                })
                .onErrorResume(error -> {
                    loggingUtility.logWebClientError("There was an error calling TMDB for movie details", error);
                    return Mono.error(error);
                })
                .block();

    }


    private MovieDetailModel prepareResponse(MovieDetailModel response) throws JsonProcessingException {

        response.setBackgroundPath(backgroundPath + response.getBackgroundPath());
        response.setPosterPath(posterPath + response.getPosterPath());
        response.setBudget(setBudget(Double.parseDouble(response.getBudget())));

        if (response.getCollectionsList() != null) {
            response.getCollectionsList().setPosterPath(posterPath + response.getCollectionsList().getPosterPath());
            response.getCollectionsList().setBackdropPath(backgroundPath + response.getCollectionsList().getBackdropPath());
        }

        return response;


    }

    private String setBudget(double movieBudget) {
        NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
        return defaultFormat.format(movieBudget);
    }


}
