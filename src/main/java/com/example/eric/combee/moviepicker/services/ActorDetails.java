package com.example.eric.combee.moviepicker.services;

import com.example.eric.combee.moviepicker.model.request.ActorRequest;
import com.example.eric.combee.moviepicker.model.response.ActorDetailModel;
import com.example.eric.combee.moviepicker.model.response.ActorResponse;
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

@Service
public class ActorDetails {

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

    public Mono<ActorResponse> gatherActorDetails(ActorRequest request) {

        String name =request.getFirstName() + " " + request.getLastName();

        return webClient.get().
                uri(uriBuilder -> uriBuilder
                        .path("search/person")
                        .queryParam("query",name)
                        .queryParam("include_adult",false)
                        .queryParam("language","en-US")
                        .queryParam("page",1)
                        .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, key)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("client error: " + response.statusCode().value() + " Not Found")))
                .bodyToMono(ActorDetailModel.class)
                .retryWhen(Retry.fixedDelay(maxAttempts, Duration.ofMillis(retryWaitTime))
                        .filter(throwable -> {
                            if (throwable instanceof WebClientResponseException) {
                                return ((WebClientResponseException) throwable).getStatusCode().is5xxServerError();
                            } else {
                                return false;
                            }
                        }))
                .map(this::prepareResponse)
                .onErrorResume(error -> {
                    loggingUtility.logWebClientError("There was an error calling TMDB for actor details", error);
                    return Mono.error(error);
                });


    }


    private ActorResponse prepareResponse(ActorDetailModel response) {

        ActorResponse actorResponse = new ActorResponse();
        int gender = response.getGender();

        actorResponse.setId(response.getId());
        actorResponse.setName(response.getName());
        actorResponse.setOriginalName(response.getOriginalName());
        actorResponse.setGender(gender == 1 ? "Female" : "Male");
        actorResponse.setImage(posterPath + response.getImage());
        actorResponse.setKnownForDepartment(response.knownForDepartment);
        actorResponse.setMovieRoles(response.getMovieRolesList());

        System.out.println(response.getName());

        return actorResponse;

    }


}
