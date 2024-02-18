package com.example.eric.combee.moviepicker.model.response.moviesearch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieSearchModel {

    @JsonProperty("backdrop_path")
    public String backgroundPath;
    public int id;
    @JsonProperty("original_title")
    public String originalTitle;
    public String overview;
    @JsonProperty("poster_path")
    public String posterPath;
    @JsonProperty("release_date")
    public String releaseDate;
    public String title;
    @JsonProperty("vote_count")
    public int voteCount;
}
