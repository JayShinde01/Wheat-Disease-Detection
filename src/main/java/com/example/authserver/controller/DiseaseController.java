package com.example.authserver.controller;

import com.example.authserver.dto.response.FlaskPredictionResponse;
import com.example.authserver.service.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/disease")
@RequiredArgsConstructor
@CrossOrigin("*")
public class DiseaseController {

    private final DiseaseService service;

    @PostMapping(
            value="/predict",
            consumes = "multipart/form-data"
    )
    public FlaskPredictionResponse predict(

            @RequestParam MultipartFile file)

            throws Exception {

        return service.detectDisease(file);

    }

}