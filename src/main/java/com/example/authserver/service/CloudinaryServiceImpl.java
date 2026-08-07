package com.example.authserver.service;

import com.example.authserver.dto.response.CloudinaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final RestClient restClient;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.upload-preset}")
    private String uploadPreset;

    @Override
    public String uploadImage(MultipartFile file) {

        ByteArrayResource resource;

        try {

            resource = new ByteArrayResource(file.getBytes()) {

                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }

            };

        } catch (IOException e) {

            throw new RuntimeException("Failed to read uploaded file", e);

        }

        MultiValueMap<String, Object> body =
                new LinkedMultiValueMap<>();

        body.add("file", resource);
        body.add("upload_preset", uploadPreset);

        CloudinaryResponse response =
                restClient.post()
                        .uri("https://api.cloudinary.com/v1_1/"
                                + cloudName
                                + "/image/upload")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .body(body)
                        .retrieve()
                        .body(CloudinaryResponse.class);

        if (response == null || response.getSecureUrl() == null) {
            throw new RuntimeException("Cloudinary upload failed.");
        }

        return response.getSecureUrl();
    }
}