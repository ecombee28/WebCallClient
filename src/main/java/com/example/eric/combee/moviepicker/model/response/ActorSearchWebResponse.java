package com.example.eric.combee.moviepicker.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorSearchWebResponse {

    public int page;
    @JsonProperty("results")
    public List<ActorDetailModel> results;
    @JsonProperty("total_pages")
    public int totalPages;
    @JsonProperty("total_results")
    public int totalResults;
}
