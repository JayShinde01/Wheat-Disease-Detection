package com.example.authserver.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authserver.dto.response.PostResponse;
import com.example.authserver.entity.Comment;

public interface CommentRepo extends JpaRepository<Comment, Long> {

	Collection<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

}
