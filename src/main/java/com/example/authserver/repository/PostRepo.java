package com.example.authserver.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authserver.dto.response.PostResponse;
import com.example.authserver.entity.Post;

public interface PostRepo extends JpaRepository<Post, Long>{

	Collection<Post> findAllByOrderByCreatedAtDesc();

}
