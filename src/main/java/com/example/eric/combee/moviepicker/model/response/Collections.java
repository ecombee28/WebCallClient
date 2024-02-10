package com.example.eric.combee.moviepicker.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collections {

    public int id;
    public String name;
    @JsonProperty("poster_path")
    public String posterPath;
    @JsonProperty("backdrop_path")
    public String backdropPath;
}
