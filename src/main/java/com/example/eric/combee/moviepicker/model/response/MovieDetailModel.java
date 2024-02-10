package com.example.eric.combee.moviepicker.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailModel {

    @JsonProperty("backdrop_path")
    public String backgroundPath;
    @JsonProperty("belongs_to_collection")
    public Collections collectionsList;
    public String budget;
    public int id;
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

}
