package com.example.authserver.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdatePostRequest {

    private String title;

    private String content;

    private MultipartFile image;
}