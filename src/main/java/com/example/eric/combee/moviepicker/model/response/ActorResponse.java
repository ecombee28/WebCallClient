package com.example.eric.combee.moviepicker.model.response;

import com.example.eric.combee.moviepicker.model.MovieRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorResponse {

    public String gender;
    public String id;
    public String knownForDepartment;
    public String name;
    public String originalName;
    public String image;
    public List<MovieRoles> movieRoles;
}
