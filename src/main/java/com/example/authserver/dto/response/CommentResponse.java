package com.example.authserver.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;

    private String content;

    private Integer userId;

    private String userName;

    private Long postId;

    private LocalDateTime createdAt;
}