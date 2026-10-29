package com.jeferson.jecommerce.dto;

import com.jeferson.jecommerce.entities.Category;

import io.swagger.v3.oas.annotations.media.Schema;

public class CategoryDTO {

	@Schema(description = "Category ID", example = "1")
	private Long id;
	
	@Schema(description = "Category name", example = "Computers")
    private String name;

    public CategoryDTO() {}

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
}
