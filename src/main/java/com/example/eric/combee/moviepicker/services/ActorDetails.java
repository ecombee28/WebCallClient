package com.example.eric.combee.moviepicker.services;

import com.example.eric.combee.moviepicker.model.MovieRoles;
import com.example.eric.combee.moviepicker.model.request.ActorRequest;
import com.example.eric.combee.moviepicker.model.response.actor.ActorResponse;
import com.example.eric.combee.moviepicker.model.response.actor.ActorSearchWebResponse;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorDetails extends ResponseException {

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
    @Value("${tmdb.path.actor}")
    private String url;

    public ActorResponse gatherActorDetails(ActorRequest request) {

        String name = request.getFirstName() + " " + request.getLastName();

        return webClient.get().
                uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("query", name)
                        .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, key)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::createResponseException)
                .onStatus(HttpStatusCode::is5xxServerError, this::createResponseException)
                .bodyToMono(ActorSearchWebResponse.class)
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
                })
                .block();


    }


    private ActorResponse prepareResponse(ActorSearchWebResponse response) {
        ActorResponse actorResponse = new ActorResponse();

        try {
            String gender = response.getResults().get(0).getGender() == 1 ? "Female" : "Male";
            String profilePicture = posterPath + response.getResults().get(0).getImage();

            actorResponse.setOriginalName(response.getResults().get(0).getOriginalName());
            actorResponse.setId(response.getResults().get(0).getId());
            actorResponse.setName(response.getResults().get(0).getName());
            actorResponse.setImage(profilePicture);
            actorResponse.setKnownForDepartment(response.getResults().get(0).getKnownForDepartment());
            actorResponse.setGender(gender);

            List<MovieRoles> movieList = response.getResults().get(0).movieRolesList.stream().map(movie -> {
                        MovieRoles movieRoles = new MovieRoles();
                        movieRoles.setPosterPath(posterPath + movie.getPosterPath());
                        movieRoles.setMediaType(movie.getMediaType());
                        movieRoles.setTitle(movie.getTitle());
                        return movieRoles;
                    })
                    .collect(Collectors.toList());

            actorResponse.setMovieRoles(movieList);

            loggingUtility.logInfo(actorResponse, "Successfully mapped new actor request");
        } catch (Exception ex) {

        }

        return actorResponse;

    }


}
