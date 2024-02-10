package com.example.eric.combee.moviepicker.model.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull
public class Response {

    public String message;
    public int code;
    public String status;
}
