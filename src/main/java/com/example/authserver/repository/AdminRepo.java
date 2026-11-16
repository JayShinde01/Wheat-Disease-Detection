package com.example.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authserver.entity.Admin;

public interface AdminRepo extends JpaRepository<Admin, String> {

}
