package com.example.authserver.service;

import com.example.authserver.dto.request.CommentRequest;
import com.example.authserver.dto.request.CreatePostRequest;
import com.example.authserver.dto.request.UpdatePostRequest;
import com.example.authserver.dto.response.CommentResponse;
import com.example.authserver.dto.response.PostResponse;
import com.example.authserver.entity.Comment;
import com.example.authserver.entity.Post;
import com.example.authserver.entity.PostLike;
import com.example.authserver.entity.User;
import com.example.authserver.repository.CommentRepo;
import com.example.authserver.repository.PostLikeRepo;
import com.example.authserver.repository.PostRepo;
import com.example.authserver.repository.UserRepo;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final PostRepo postRepository;
    private final CommentRepo commentRepository;
    private final PostLikeRepo postLikeRepository;
    private final UserRepo userRepo;

    private final CloudinaryService cloudinaryService;


    // =========================
    // CREATE POST
    // =========================

    @Override
    public PostResponse createPost(
            CreatePostRequest request,
            Integer userId
    ) throws Exception {

        User user = getUser(userId);

        String imageUrl = null;

        MultipartFile image = request.getImage();

        if (image != null && !image.isEmpty()) {

            imageUrl =
                    cloudinaryService.uploadImage(image);
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .imageUrl(imageUrl)
                .user(user)
                .likeCount(0)
                .commentCount(0)
                .build();

        post = postRepository.save(post);

        return mapPost(post);
    }


    // =========================
    // GET ALL POSTS
    // =========================

    @Override
    public List<PostResponse> getAllPosts() {

        return postRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapPost)
                .toList();
    }


    // =========================
    // GET SINGLE POST
    // =========================

    @Override
    public PostResponse getPostById(
            Long postId
    ) {

        Post post = getPost(postId);

        return mapPost(post);
    }


    // =========================
    // UPDATE POST
    // =========================

    @Override
    public PostResponse updatePost(
            Long postId,
            UpdatePostRequest request,
            Integer userId
    ) {

        Post post = getPost(postId);

        checkPostOwner(post, userId);

        if (request.getTitle() != null) {

            post.setTitle(
                    request.getTitle()
            );
        }

        if (request.getContent() != null) {

            post.setContent(
                    request.getContent()
            );
        }

        if (request.getImage() != null
                && !request.getImage().isEmpty()) {

            try {

                String imageUrl =
                        cloudinaryService.uploadImage(
                                request.getImage()
                        );

                post.setImageUrl(imageUrl);

            } catch (Exception e) {

                throw new RuntimeException(
                        "Failed to upload image",
                        e
                );
            }
        }

        post = postRepository.save(post);

        return mapPost(post);
    }


    // =========================
    // DELETE POST
    // =========================

    @Override
    public void deletePost(
            Long postId,
            Integer userId
    ) {

        Post post = getPost(postId);

        checkPostOwner(
                post,
                userId
        );

        postRepository.delete(post);
    }


    // =========================
    // LIKE
    // =========================

    @Override
    public void likePost(
            Long postId,
            Integer userId
    ) {

        Post post = getPost(postId);

        User user = getUser(userId);

        boolean alreadyLiked =
                postLikeRepository
                        .existsByUserIdAndPostId(
                                userId,
                                postId
                        );

        if (alreadyLiked) {
            return;
        }

        PostLike like =
                PostLike.builder()
                        .user(user)
                        .post(post)
                        .build();

        postLikeRepository.save(like);

        post.setLikeCount(
                post.getLikeCount() + 1
        );

        postRepository.save(post);
    }


    // =========================
    // UNLIKE
    // =========================

    @Override
    public void unlikePost(
            Long postId,
            Integer userId
    ) {

        Post post = getPost(postId);

        PostLike like =
                postLikeRepository
                        .findByUserIdAndPostId(
                                userId,
                                postId
                        )
                        .orElse(null);

        if (like == null) {
            return;
        }

        postLikeRepository.delete(like);

        post.setLikeCount(
                Math.max(
                        0,
                        post.getLikeCount() - 1
                )
        );

        postRepository.save(post);
    }


    // =========================
    // ADD COMMENT
    // =========================

    @Override
    public CommentResponse addComment(
            Long postId,
            CommentRequest request,
            Integer userId
    ) {

        Post post = getPost(postId);

        User user = getUser(userId);

        Comment comment =
                Comment.builder()
                        .content(request.getContent())
                        .user(user)
                        .post(post)
                        .build();

        comment =
                commentRepository.save(comment);

        post.setCommentCount(
                post.getCommentCount() + 1
        );

        postRepository.save(post);

        return mapComment(comment);
    }


    // =========================
    // GET COMMENTS
    // =========================

    @Override
    public List<CommentResponse> getComments(Long postId) {
        getPost(postId);

        return commentRepository
                .findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(this::mapComment)
                .toList();
    }


    // =========================
    // DELETE COMMENT
    // =========================

    @Override
    public void deleteComment(
            Long commentId,
            Integer userId
    ) {

        Comment comment =
                commentRepository
                        .findById(commentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Comment not found"
                                )
                        );

        if (!comment.getUser()
                .getId()
                .equals(userId)) {

            throw new RuntimeException(
                    "You can delete only your own comment"
            );
        }

        Post post = comment.getPost();

        commentRepository.delete(comment);

        post.setCommentCount(
                Math.max(
                        0,
                        post.getCommentCount() - 1
                )
        );

        postRepository.save(post);
    }


    // =========================
    // HELPERS
    // =========================

    private User getUser(
            Integer userId
    ) {

        return userRepo.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"
                        )
                );
    }


    private Post getPost(
            Long postId
    ) {

        return postRepository.findById(postId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Post not found"
                        )
                );
    }


    private void checkPostOwner(
            Post post,
            Integer userId
    ) {

        if (!post.getUser()
                .getId()
                .equals(userId)) {

            throw new RuntimeException(
                    "You can modify only your own post"
            );
        }
    }


    // =========================
    // POST RESPONSE
    // =========================

    private PostResponse mapPost(Post post) {

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .userId(post.getUser().getId())
                .userName(post.getUser().getName())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }


    // =========================
    // COMMENT RESPONSE
    // =========================

    private CommentResponse mapComment(Comment comment) {

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}