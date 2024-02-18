package com.example.eric.combee.moviepicker.model.response.moviesearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieSearchResponse {

    public int totalResults;
    @JsonProperty("results")
    public List<MovieSearchModel> results;
}
