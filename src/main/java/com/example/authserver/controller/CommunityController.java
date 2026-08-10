package com.example.authserver.controller;


import com.example.authserver.dto.request.CommentRequest;
import com.example.authserver.dto.request.CreatePostRequest;
import com.example.authserver.dto.request.UpdatePostRequest;
import com.example.authserver.dto.response.CommentResponse;
import com.example.authserver.dto.response.PostResponse;
import com.example.authserver.security.CustomUserDetails;
import com.example.authserver.service.CommunityService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/community")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommunityController {


    private final CommunityService communityService;


    // ==========================================
    // CREATE POST
    // ==========================================

    @PostMapping(
            value = "/posts",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<PostResponse> createPost(

            @ModelAttribute CreatePostRequest request,

            Authentication authentication

    ) throws Exception {

        Integer userId =
                getUserId(authentication);

        PostResponse response =
                communityService.createPost(
                        request,
                        userId
                );

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // GET ALL POSTS
    // ==========================================

    @GetMapping("/posts")
    public ResponseEntity<List<PostResponse>> getAllPosts() {

        return ResponseEntity.ok(
                communityService.getAllPosts()
        );
    }


    // ==========================================
    // GET SINGLE POST
    // ==========================================

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> getPostById(

            @PathVariable Long postId

    ) {

        return ResponseEntity.ok(
                communityService.getPostById(
                        postId
                )
        );
    }


    // ==========================================
    // UPDATE POST
    // ==========================================

    @PutMapping(
            value = "/posts/{postId}",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<PostResponse> updatePost(

            @PathVariable Long postId,

            @ModelAttribute UpdatePostRequest request,

            Authentication authentication

    ) {

        Integer userId =
                getUserId(authentication);

        PostResponse response =
                communityService.updatePost(
                        postId,
                        request,
                        userId
                );

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // DELETE POST
    // ==========================================

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(

            @PathVariable Long postId,

            Authentication authentication

    ) {

        Integer userId =
                getUserId(authentication);

        communityService.deletePost(
                postId,
                userId
        );

        return ResponseEntity.noContent().build();
    }


    // ==========================================
    // LIKE POST
    // ==========================================

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Void> likePost(

            @PathVariable Long postId,

            Authentication authentication

    ) {

        Integer userId =
                getUserId(authentication);

        communityService.likePost(
                postId,
                userId
        );

        return ResponseEntity.ok().build();
    }


    // ==========================================
    // UNLIKE POST
    // ==========================================

    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<Void> unlikePost(

            @PathVariable Long postId,

            Authentication authentication

    ) {

        Integer userId =
                getUserId(authentication);

        communityService.unlikePost(
                postId,
                userId
        );

        return ResponseEntity.noContent().build();
    }


    // ==========================================
    // ADD COMMENT
    // ==========================================

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(

            @PathVariable Long postId,

            @RequestBody CommentRequest request,

            Authentication authentication

    ) {

        Integer userId =
                getUserId(authentication);

        CommentResponse response =
                communityService.addComment(
                        postId,
                        request,
                        userId
                );

        return ResponseEntity.ok(response);
    }


    // ==========================================
    // GET COMMENTS
    // ==========================================

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(

            @PathVariable Long postId

    ) {

        return ResponseEntity.ok(
                communityService.getComments(
                        postId
                )
        );
    }


    // ==========================================
    // DELETE COMMENT
    // ==========================================

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(

            @PathVariable Long commentId,

            Authentication authentication

    ) {

        Integer userId =
                getUserId(authentication);

        communityService.deleteComment(
                commentId,
                userId
        );

        return ResponseEntity.noContent().build();
    }


    // ==========================================
    // GET LOGGED-IN USER ID
    // ==========================================

    private Integer getUserId(
            Authentication authentication
    ) {

        if (authentication == null) {

            throw new RuntimeException(
                    "User not authenticated"
            );
        }


        if (!(authentication.getPrincipal()
                instanceof CustomUserDetails)) {

            throw new RuntimeException(
                    "Invalid authentication principal"
            );
        }


        CustomUserDetails userDetails =
                (CustomUserDetails)
                        authentication.getPrincipal();


        return userDetails
                .getUser()
                .getId();
    }
}