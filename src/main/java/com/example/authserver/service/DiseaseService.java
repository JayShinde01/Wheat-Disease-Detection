package com.example.authserver.service;

import com.example.authserver.client.MlPredictionClient;
import com.example.authserver.dto.response.FlaskPredictionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DiseaseService {

    private final MlPredictionClient client;

    public FlaskPredictionResponse detectDisease(
            MultipartFile file)
            throws Exception {

        FlaskPredictionResponse prediction =
                client.predict(file);

        return prediction;

    }

}