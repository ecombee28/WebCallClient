package com.example.eric.combee.moviepicker.model.response.cast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CastResponse {

    public int id;
    public String gender;
    public String knownForDepartment;
    public String name;
    public double popularity;
    public String profilePath;
    public String characterName;
}
