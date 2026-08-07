package com.example.authserver.service;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.authserver.dto.request.LoginRequest;
import com.example.authserver.dto.request.RegisterRequest;
import com.example.authserver.dto.response.AuthResponse;
import com.example.authserver.entity.User;

public interface AuthService {

    User register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    String processGoogleLogin(OAuth2User oAuth2User);

}