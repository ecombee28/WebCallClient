package com.example.eric.combee.moviepicker.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieSearchRequest {

    @NonNull
    public String movieName = "";
    public String releaseYear = "";
    public int numberOfResults = 0;

}
