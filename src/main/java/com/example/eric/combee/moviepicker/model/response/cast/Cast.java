package com.example.eric.combee.moviepicker.model.response.cast;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cast {

    public int gender;
    public int id;
    @JsonProperty("known_for_department")
    public String knownForDepartment;
    public String name;
    @JsonProperty("original_name")
    public String originalName;
    public double popularity;
    @JsonProperty("profile_path")
    public String profilePath;
    public String character;

}
