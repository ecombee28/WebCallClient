package com.example.eric.combee.moviepicker.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorRequest {

    @NonNull
    public String firstName;
    public String lastName;
    public String knownDepartment;
    public String gender;
}
