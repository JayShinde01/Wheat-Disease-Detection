package com.example.authserver.dto.response;



import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponse {

    private Long id;

    private String title;

    private String content;

    private String imageUrl;

    private Integer userId;

    private String userName;

    private Integer likeCount;

    private Integer commentCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}