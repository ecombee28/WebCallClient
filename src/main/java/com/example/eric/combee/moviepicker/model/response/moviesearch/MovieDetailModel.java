package com.example.eric.combee.moviepicker.model.response.moviesearch;

import com.example.eric.combee.moviepicker.model.response.cast.CastResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailModel {

    public int id;
    @JsonProperty("backdrop_path")
    public String backgroundPath;
    @JsonProperty("belongs_to_collection")
    public Collections collectionsList;
    public String budget;
    @JsonProperty("imdb_id")
    public String imdbId;
    @JsonProperty("original_title")
    public String originalTitle;
    public String overview;
    @JsonProperty("poster_path")
    public String posterPath;
    @JsonProperty("release_date")
    public String releaseDate;
    public String tagline;
    public String title;
    @JsonProperty("vote_count")
    public int voteCount;
    public List<CastResponse> castList;

}
