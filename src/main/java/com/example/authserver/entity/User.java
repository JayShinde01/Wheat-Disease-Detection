package com.example.authserver.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.authserver.enums.Provider;
import com.example.authserver.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false)
	private String name;
	
	
	@NotBlank(message = "email required..")
	@Column(nullable = false,unique = true)
	@Email
	private String email;
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Provider provider;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	private Boolean enabled;
	
	
	@CreationTimestamp
	private LocalDateTime createdAt ;
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
