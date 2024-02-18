package com.example.eric.combee.moviepicker.services;

import com.example.eric.combee.moviepicker.model.response.cast.CastResponse;
import com.example.eric.combee.moviepicker.model.response.cast.MovieCastResponse;
import com.example.eric.combee.moviepicker.utility.LoggingUtility;
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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class MovieCast extends ResponseException {
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
    @Value("${tmdb.poster.path.url}")
    private String posterPath;
    @Value("${tmdb.path.movie.cast}")
    private String url;

    public List<CastResponse> getMovieCast(String movieId) {

        return webClient.get().uri(
                        uriBuilder -> uriBuilder
                                .path(url)
                                .queryParam("language", "en-US")
                                .build(movieId))
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, key)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::createResponseException)
                .onStatus(HttpStatusCode::is5xxServerError, this::createResponseException)
                .bodyToMono(MovieCastResponse.class)
                .retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofMillis(retryWaitTime))
                        .filter(throwable -> {
                            if (throwable instanceof WebClientResponseException) {
                                return ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
                            } else {
                                return false;
                            }
                        }))
                .map(this::prepareCastResponse)
                .onErrorResume(error -> {
                    loggingUtility.logWebClientError("There was an error calling TMDB for actor details", error);
                    return Mono.error(error);
                })
                .block();

    }

    private List<CastResponse> prepareCastResponse(MovieCastResponse response) {

        List<CastResponse> castList = new ArrayList<>();

        try {
            for (int i = 0; i < 5; i++) {
                CastResponse castResponse = new CastResponse();
                castResponse.setId(response.getCastList().get(i).getId());
                castResponse.setName(response.getCastList().get(i).getName());
                castResponse.setCharacterName(response.getCastList().get(i).getCharacter());
                castResponse.setPopularity(response.getCastList().get(i).getPopularity());
                castResponse.setGender(setGender(response.getCastList().get(i).getGender()));
                castResponse.setProfilePath(setupValue(response.getCastList().get(i).getProfilePath(), pic -> posterPath + pic));
                castResponse.setKnownForDepartment(response.getCastList().get(i).getKnownForDepartment());
                castList.add(castResponse);
            }
        } catch (Exception ex) {

        }

        return castList;
    }

    private String setGender(int genderNumber) {
        return genderNumber == 1 ? "Female" : "Male";

    }

    private <T> T setupValue(T value, Function<T, T> setterFunction) {
        if (setterFunction != null && value != null) {
            return setterFunction.apply(value);
        }

        return value;
    }

}
