package com.example.authserver.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CloudinaryResponse {

    @JsonProperty("secure_url")
    private String secureUrl;

}
