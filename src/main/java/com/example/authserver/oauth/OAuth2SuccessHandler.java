package com.example.authserver.oauth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.authserver.entity.User;
import com.example.authserver.enums.Provider;
import com.example.authserver.enums.Role;
import com.example.authserver.jwt.JwtService;
import com.example.authserver.repository.UserRepo;
import com.example.authserver.security.CustomUserDetails;
import com.example.authserver.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepo repository;
    private final JwtService jwtService;
    
    @Value("${app.frontend-url}")
    private String frontendUrl ;
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        OAuth2User oAuth2User =
                (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = repository.findByEmail(email)
                .orElseGet(() -> repository.save(
                        User.builder()
                                .name(name)
                                .email(email)
                                .role(Role.USER)
                                .provider(Provider.GOOGLE)
                                .enabled(true)
                                .build()
                ));

        String token = jwtService.generateToken(
                new CustomUserDetails(user));

        response.sendRedirect(
                frontendUrl
                + "/oauth-success"
                + "?token=" + token
                + "&email=" + java.net.URLEncoder.encode(
                        email,
                        java.nio.charset.StandardCharsets.UTF_8
                )
        );

    }
}
