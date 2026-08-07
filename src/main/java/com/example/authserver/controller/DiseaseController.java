package com.example.authserver.controller;

import com.example.authserver.dto.response.DiseasePredictionResponse;
import com.example.authserver.entity.User;
import com.example.authserver.security.CustomUserDetails;
import com.example.authserver.service.DiseaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/disease")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DiseaseController {

    private final DiseaseService diseaseService;

    @PostMapping(
            value = "/predict",
            consumes = "multipart/form-data"
    )
    public DiseasePredictionResponse predict(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) throws Exception {


        if(authentication == null){
            throw new RuntimeException(
                "User not authenticated"
            );
        }


        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();


        User user = userDetails.getUser();


        return diseaseService.detectDisease(
                file,
                user
        );
    }
}