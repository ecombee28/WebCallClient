package com.example.eric.combee.moviepicker.model.response;

import com.example.eric.combee.moviepicker.model.request.MovieSearchRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequest {

    public int pages;

    @JsonProperty("results")
    public List<MovieSearchRequest> results;
}
