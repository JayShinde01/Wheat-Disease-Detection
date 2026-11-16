package com.example.authserver.client;

import com.example.authserver.dto.request.RagAssistRequest;
import com.example.authserver.dto.response.RagAssistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class RagClient {

    private final RestClient ragRestClient;

    public RagAssistResponse assist(RagAssistRequest request) {

        System.out.println("in rag assist");

        try {

            RagAssistResponse response =
                    ragRestClient.post()
                            .uri("/assist")
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(request)
                            .retrieve()
                            .body(RagAssistResponse.class);

            if (response == null) {
                return RagAssistResponse.builder()
                        .available(false)
                        .disease(request.getDisease())
                        .answer("Agricultural AI assistance is temporarily unavailable.")
                        .sources(java.util.Collections.emptyList())
                        .build();
            }

            response.setAvailable(true);

            if (response.getDisease() == null) {
                response.setDisease(request.getDisease());
            }

            System.out.println("RAG response: " + response);

            return response;

        } catch (Exception e) {

            e.printStackTrace();

            System.out.println("RAG failed: " + e.getMessage());

            return RagAssistResponse.builder()
                    .available(false)
                    .disease(request.getDisease())
                    .answer("Agricultural AI assistance is temporarily unavailable.")
                    .sources(java.util.Collections.emptyList())
                    .build();
        }
    }
}