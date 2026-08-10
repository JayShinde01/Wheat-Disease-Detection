package com.example.authserver.service;

import java.util.List;

import com.example.authserver.dto.request.CommentRequest;
import com.example.authserver.dto.request.CreatePostRequest;
import com.example.authserver.dto.request.UpdatePostRequest;
import com.example.authserver.dto.response.CommentResponse;
import com.example.authserver.dto.response.PostResponse;

public interface CommunityService {

    PostResponse createPost(
            CreatePostRequest request,
            Integer userId
    ) throws Exception;

    List<PostResponse> getAllPosts();

    PostResponse getPostById(
            Long postId
    );

    PostResponse updatePost(
            Long postId,
            UpdatePostRequest request,
            Integer userId
    );

    void deletePost(
            Long postId,
            Integer userId
    );

    void likePost(
            Long postId,
            Integer userId
    );

    void unlikePost(
            Long postId,
            Integer userId
    );

    CommentResponse addComment(
            Long postId,
            CommentRequest request,
            Integer userId
    );

    List<CommentResponse> getComments(
            Long postId
    );

    void deleteComment(
            Long commentId,
            Integer userId
    );
}