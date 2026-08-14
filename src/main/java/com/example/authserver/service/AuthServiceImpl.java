package com.example.authserver.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.authserver.repository.UserRepo;
import com.example.authserver.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepo userRepo;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;
	
	
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

}
