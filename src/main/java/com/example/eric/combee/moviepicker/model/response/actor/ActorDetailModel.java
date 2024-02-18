package com.example.eric.combee.moviepicker.model.response.actor;

import com.example.eric.combee.moviepicker.model.MovieRoles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorDetailModel {

    public int gender;
    public String id;
    @JsonProperty("known_for_department")
    public String knownForDepartment;
    public String name;
    @JsonProperty("original_name")
    public String originalName;
    @JsonProperty("profile_path")
    public String image;
    @JsonProperty("known_for")
    public List<MovieRoles> movieRolesList;
}
