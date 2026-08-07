package com.example.authserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FlaskPredictionResponse {

    private String prediction;

    private Double confidence;

    @JsonProperty("image_url")
    private String imageUrl;

}
