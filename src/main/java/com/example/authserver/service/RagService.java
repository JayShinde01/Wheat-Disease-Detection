package com.example.authserver.service;

import com.example.authserver.client.RagClient;
import com.example.authserver.dto.request.RagAssistRequest;
import com.example.authserver.dto.response.RagAssistResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RagService {

    private final RagClient ragClient;

    public RagAssistResponse assist(
            String disease) {

        return ragClient.assist(
                new RagAssistRequest(disease)
        );
    }
}