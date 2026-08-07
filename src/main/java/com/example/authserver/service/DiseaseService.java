package com.example.authserver.service;

import com.example.authserver.client.MlPredictionClient;
import com.example.authserver.dto.response.DiseasePredictionResponse;
import com.example.authserver.dto.response.FlaskPredictionResponse;
import com.example.authserver.entity.DiseaseDetection;
import com.example.authserver.entity.User;
import com.example.authserver.repository.DiseaseDetectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiseaseService {

    private final MlPredictionClient mlPredictionClient;
    private final CloudinaryService cloudinaryService;
    private final DiseaseDetectionRepository diseaseDetectionRepository;

    public DiseasePredictionResponse detectDisease(
            MultipartFile file,
            User user) throws Exception {

        // Step 1: ML Prediction
        FlaskPredictionResponse prediction =
                mlPredictionClient.predict(file);

        // Step 2: Upload to Cloudinary
        String imageUrl =
                cloudinaryService.uploadImage(file);

        // Step 3: Save to Database
        DiseaseDetection detection = DiseaseDetection.builder()
                .user(user)
                .imageUrl(imageUrl)
                .disease(prediction.getPrediction())
                .confidence(prediction.getConfidence())
                .detectedAt(LocalDateTime.now())
                .build();

        detection = diseaseDetectionRepository.save(detection);

        // Step 4: Return Response
        return DiseasePredictionResponse.builder()
                .predictionId(detection.getId())
                .disease(prediction.getPrediction())
                .confidence(prediction.getConfidence())
                .imageUrl(imageUrl)
                .build();
    }
}