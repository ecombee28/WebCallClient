package com.example.eric.combee.moviepicker.model.response.moviesearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieRequest {

    public int page;
    @JsonProperty("results")
    public List<MovieSearchModel> results;
    @JsonProperty("total_pages")
    public int totalPages;
    @JsonProperty("total_results")
    public int totalResults;

}
