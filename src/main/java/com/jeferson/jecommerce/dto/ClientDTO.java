package com.jeferson.jecommerce.dto;

import com.jeferson.jecommerce.entities.User;

import io.swagger.v3.oas.annotations.media.Schema;

public class ClientDTO {

	@Schema(description = "Client ID", example = "1")
	private Long id;
	
	@Schema(description = "Client first name", example = "Carlos")
	private String name;
	
	public ClientDTO() {}
	
	public ClientDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public ClientDTO(User entity) {
		id = entity.getId();
		name = entity.getName();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
