package com.example.authserver.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
	
	@NotBlank(message = "name is required")
	private String name;
	
	@NotBlank(message = "email is requied")
	@Email
	private String email;
	
	@NotBlank(message = "enter password")
	@Size(min = 6, max = 20)
	private String password;
	
}
