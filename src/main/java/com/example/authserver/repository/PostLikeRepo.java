package com.example.authserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authserver.entity.Comment;
import com.example.authserver.entity.PostLike;

public interface PostLikeRepo extends JpaRepository<PostLike, Long>{

	boolean existsByUserIdAndPostId(Integer userId, Long postId);

	Optional<PostLike> findByUserIdAndPostId(Integer userId, Long postId);

}
