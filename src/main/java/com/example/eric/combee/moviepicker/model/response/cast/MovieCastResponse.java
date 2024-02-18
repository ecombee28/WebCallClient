package com.example.eric.combee.moviepicker.model.response.cast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieCastResponse {

    public int id;
    @JsonProperty("cast")
    public List<Cast> castList;
}
