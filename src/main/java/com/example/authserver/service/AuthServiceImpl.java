package com.example.authserver.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.authserver.dto.request.LoginRequest;
import com.example.authserver.dto.request.RegisterRequest;
import com.example.authserver.dto.response.AuthResponse;
import com.example.authserver.entity.User;
import com.example.authserver.enums.Provider;
import com.example.authserver.enums.Role;
import com.example.authserver.jwt.JwtService;
import com.example.authserver.repository.PasswordResetTokenRepository;
import com.example.authserver.repository.UserRepo;
import com.example.authserver.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import com.example.authserver.dto.request.ForgotPasswordRequest;
import com.example.authserver.dto.request.ResetPasswordRequest;
import com.example.authserver.entity.PasswordResetToken;
import com.example.authserver.repository.PasswordResetTokenRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepo userRepo;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final EmailService emailService;
    @Value("${app.frontend-url}")
    private String frontendUrl;
	@Override
	public User register(RegisterRequest request) {
		Optional<User> op = userRepo.findByEmail(request.getEmail());
		
		if(op.isPresent()) {
			User user = op.get();
			if(user.getPassword() != null)
			return null;
			
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			return userRepo.save(user);
		}
		User user = User.builder()
				.email(request.getEmail())
				.name(request.getName())
				.role(Role.USER)
				.provider(Provider.LOCAL)
				.enabled(true)
				.password(passwordEncoder.encode(request.getPassword()))
				.build();
		return userRepo.save(user);
	}


	@Override
	public AuthResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
				);
		  UserDetails user = (UserDetails) authentication.getPrincipal();
		  String token = jwtService.generateToken(user);
		return AuthResponse.builder()
				.token(token)
				.message("log in successfull")
				.email(user.getUsername())
				.build();
	}
	
	@Override
	public String processGoogleLogin(OAuth2User oAuth2User) {

	    String email = oAuth2User.getAttribute("email");
	    String name = oAuth2User.getAttribute("name");

	    User user = userRepo.findByEmail(email)
	            .orElseGet(() -> {

	                User newUser = User.builder()
	                        .name(name)
	                        .email(email)
	                        .provider(Provider.GOOGLE)
	                        .role(Role.USER)
	                        .enabled(true)
	                        .build();

	                return userRepo.save(newUser);
	            });

	    return jwtService.generateToken(
	            new CustomUserDetails(user)
	    );

	}
	@Override
	public void forgotPassword(ForgotPasswordRequest request) {

	    Optional<User> optionalUser = userRepo.findByEmail(request.getEmail());

	    if (optionalUser.isEmpty())return;
	    

	    User user = optionalUser.get();

	    String token = UUID.randomUUID().toString();

	    PasswordResetToken resetToken = PasswordResetToken.builder()
	                    .token(token)
	                    .user(user)
	                    .expiresAt(
	                            LocalDateTime.now()
	                                    .plusMinutes(15)
	                    )
	                    .used(false)
	                    .build();

	    passwordResetTokenRepository.save(resetToken);

	    String resetLink = frontendUrl 
	    		           + "/reset-password?token="
	                       + token;

	    emailService.sendPasswordResetEmail(
	            user.getEmail(),
	            resetLink
	    );
	}
	@Override
	public void resetPassword(
	        ResetPasswordRequest request) {

	    PasswordResetToken resetToken =
	            passwordResetTokenRepository
	                    .findByToken(request.getToken())
	                    .orElseThrow(() ->
	                            new RuntimeException(
	                                    "Invalid reset token"
	                            )
	                    );

	    if (resetToken.isUsed()) {

	        throw new RuntimeException(
	                "Reset token has already been used"
	        );
	    }

	    if (
	        resetToken.getExpiresAt()
	                .isBefore(LocalDateTime.now())
	    ) {

	        throw new RuntimeException(
	                "Reset token has expired"
	        );
	    }

	    User user =
	            resetToken.getUser();

	    user.setPassword(
	            passwordEncoder.encode(
	                    request.getPassword()
	            )
	    );

	    userRepo.save(user);

	    resetToken.setUsed(true);

	    passwordResetTokenRepository.save(
	            resetToken
	    );
	}

}
