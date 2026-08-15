package com.example.authserver.service;

public interface EmailService {

    void sendPasswordResetEmail(
            String email,
            String resetLink
    );
}