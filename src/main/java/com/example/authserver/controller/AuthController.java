package com.example.authserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.authserver.dto.request.ForgotPasswordRequest;
import com.example.authserver.dto.request.LoginRequest;
import com.example.authserver.dto.request.RegisterRequest;
import com.example.authserver.dto.request.ResetPasswordRequest;
import com.example.authserver.dto.response.AuthResponse;
import com.example.authserver.entity.User;
import com.example.authserver.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

	
	private final AuthService authService;
	
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req){
		User user = authService.register(req);
		if(user == null)return ResponseEntity.internalServerError().body("email alredy exists");
		return ResponseEntity.status(201).body(user);
	}
	@PostMapping("/login")
	public AuthResponse login(
	        @RequestBody @Valid LoginRequest request){

	    return authService.login(request);

	}
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(
	        @RequestBody ForgotPasswordRequest request) {

	    authService.forgotPassword(request);

	    return ResponseEntity.ok(
	            "If the email exists, a password reset link has been sent."
	    );
	}
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(
	        @RequestBody ResetPasswordRequest request) {

	    authService.resetPassword(request);

	    return ResponseEntity.ok(
	            "Password reset successfully."
	    );
	}
	@GetMapping("/")
	public ResponseEntity<?> home(){
		
		return ResponseEntity.ok("Server is running");
	}
}
