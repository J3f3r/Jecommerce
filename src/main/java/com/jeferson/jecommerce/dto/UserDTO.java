package com.jeferson.jecommerce.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.jeferson.jecommerce.entities.User;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserDTO {

	@Schema(description = "User ID", example = "1")
	private Long id;
	
	@Schema(description = "User name", example = "Maria Brown")
	private String name;
	
	@Schema(description = "User email address", example = "maria@gmail.com")
	private String email;
	
	@Schema(description = "User phone number", example = "(99)99999-9999")
	private String phone;
	
	@Schema(description = "User birth date", example = "1998-07-13")
	private LocalDate birthDate;
	
	@Schema(description = "User roles")
	List<String> roles = new ArrayList<>();
	
	public UserDTO() {}

	public UserDTO(Long id, String name, String email, String phone, LocalDate birthDate, List<String> roles) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.birthDate = birthDate;
		this.roles = roles;
	}
	
	public UserDTO(User entity) {
		id = entity.getId();
		name = entity.getName();
		email = entity.getEmail();
		phone = entity.getPhone();
		birthDate = entity.getBirthDate();
		
		for (GrantedAuthority role : entity.getRoles()) {
			roles.add(role.getAuthority());
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public List<String> getRoles() {
		return roles;
	}
}
