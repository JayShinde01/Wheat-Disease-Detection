package com.example.authserver.client;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import com.example.authserver.dto.response.FlaskPredictionResponse;

@Service
@RequiredArgsConstructor
public class MlPredictionClient {

    private final RestClient restClient;

    @Value("${ml.service.url}")
    private String mlUrl;

    public FlaskPredictionResponse predict(MultipartFile file)
            throws Exception {

        ByteArrayResource resource =
                new ByteArrayResource(file.getBytes()) {

                    @Override
                    public String getFilename() {

                        return file.getOriginalFilename();

                    }

                };

        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        body.add("file", resource);

        return restClient.post()

                .uri(mlUrl + "/api/predict")

                .contentType(MediaType.MULTIPART_FORM_DATA)

                .body(body)

                .retrieve()

                .body(FlaskPredictionResponse.class);

    }

}