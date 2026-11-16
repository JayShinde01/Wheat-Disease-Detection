package com.example.authserver.controller;


import com.example.authserver.dto.request.RagAssistRequest;
import com.example.authserver.dto.response.RagAssistResponse;
import com.example.authserver.service.RagService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    @PostMapping("/assist")
    public RagAssistResponse assist(
            @RequestBody RagAssistRequest request) {

        return ragService.assist(
                request.getDisease()
        );
    }
}