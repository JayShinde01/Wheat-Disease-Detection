package com.example.authserver.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin {
	 
	 @Id
	 @Email
	 @NotBlank(message = "email required..")
	 private String email;
	
	 @NotBlank(message = "password required..")
	 private String password;
}
