package com.example.authserver.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

	@NotBlank(message = "email is needed")
	@Email
	private String email;
	
	@NotBlank
	private String password;
}
