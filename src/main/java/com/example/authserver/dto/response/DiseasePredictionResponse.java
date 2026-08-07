package com.example.authserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseasePredictionResponse {

    private Long predictionId;

    private String disease;

    private Double confidence;

    private String imageUrl;

}