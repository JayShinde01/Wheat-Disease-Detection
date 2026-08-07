package com.example.authserver.controller;

import com.example.authserver.dto.response.DiseasePredictionResponse;
import com.example.authserver.entity.DiseaseDetection;
import com.example.authserver.entity.User;
import com.example.authserver.repository.UserRepo;
import com.example.authserver.security.CustomUserDetails;
import com.example.authserver.service.DiseaseService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/disease")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DiseaseController {

    private final DiseaseService diseaseService;
    private final UserRepo userRepo;

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
    
    @GetMapping("/byuser/{email}")
    public ResponseEntity<?> getDetectionsByUserId(@PathVariable String email){
    	
        Integer id = userRepo.findByEmail(email).get().getId();

    	List<DiseaseDetection> ls = diseaseService.getDetectionByUserId(id);
    	if(ls.isEmpty())return ResponseEntity.internalServerError().body("No detection found for this user");
    	return ResponseEntity.status(200).body(ls);
    }
}