package com.example.authserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RagConfig {

    @Value("${rag.base-url}")
    private String ragBaseUrl;

    @Bean
    public RestClient ragRestClient() {

        return RestClient.builder()
                .baseUrl(ragBaseUrl)
                .build();
    }
}