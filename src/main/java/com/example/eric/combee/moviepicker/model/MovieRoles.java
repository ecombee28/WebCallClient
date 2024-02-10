package com.example.eric.combee.moviepicker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieRoles {
    @JsonProperty("poster_path")
    public String posterPath;
    public String title;
    @JsonProperty("media_type")
    public String mediaType;
}
